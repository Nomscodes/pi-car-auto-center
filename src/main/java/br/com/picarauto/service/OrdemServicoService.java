package br.com.picarauto.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.repository.IClienteRepository;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.repository.IVeiculoRepository;
import br.com.picarauto.util.FilaOS;
import jakarta.annotation.PostConstruct;
import br.com.picarauto.util.ArvoreOS;
import br.com.picarauto.util.TabelaHashOS;
import br.com.picarauto.util.OrdenadorOS;
import br.com.picarauto.util.OrdenadorPorId;
import br.com.picarauto.util.OrdenadorPorNomeCliente;
import br.com.picarauto.validation.IOrdemServicoValidation;

/**
 *
 * @author Caio4breu
 */
@Service
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    private final FilaOS filaEspera = new FilaOS();
    
    private final ArvoreOS     indiceArvore = new ArvoreOS();
    private final TabelaHashOS indiceHash   = new TabelaHashOS();

    // Instâncias únicas — os ordenadores são stateless, não precisam ser recriados a cada chamada.

    // Instâncias únicas — os ordenadores são stateless, não precisam ser recriados a cada chamada.
    private final OrdenadorOS ordenadorPorId = new OrdenadorPorId();
    private final OrdenadorOS ordenadorPorNomeCliente = new OrdenadorPorNomeCliente();

    // Injetados para enriquecer OS com placa e nome do cliente
    private final IVeiculoRepository veiculoRepository;
    private final IClienteRepository clienteRepository;

    public OrdemServicoService(IOrdemServicoRepository repository,
            IOrdemServicoValidation validation,
            IVeiculoRepository veiculoRepository,
            IClienteRepository clienteRepository) {
        super(repository, validation);
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Sincroniza FilaOS, ArvoreOS e TabelaHashOS com o banco na inicialização.
     * Usa @PostConstruct porque no construtor os repositórios ainda não foram injetados.
     */
    @PostConstruct
    public void sincronizarEstruturasComBanco() {
        List<OrdemServicoModel> ativas = findAllActiveEnriquecido();

        ativas.stream()
              .sorted(java.util.Comparator.comparingLong(os -> os.getId() != null ? os.getId() : 0L))
              .forEach(os -> {
                  filaEspera.enfileirar(os);
                  indiceArvore.inserir(os);
                  indiceHash.inserir(os);
              });
    }
    
    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        if (entity.getDataAbertura() == null) {
            entity.setDataAbertura(LocalDate.now());
        }
    }

    // Enfileiramento (afterInsert)
    @Override
    protected void afterInsert(OrdemServicoModel savedEntity, OrdemServicoModel old) {
        filaEspera.enfileirar(savedEntity);
        indiceArvore.inserir(savedEntity);
        indiceHash.inserir(savedEntity);
    }

    // Consulta da fila
    public FilaOS getFilaEspera() {
        return filaEspera;
    }
    
    public ArvoreOS getIndiceArvore() {
        return indiceArvore;
    }

    public TabelaHashOS getIndiceHash() {
        return indiceHash;
    }

    /**
     * Busca uma OS por id usando ArvoreOS — O(log n).
     */
    public OrdemServicoModel buscarPorId(Long id) {
        return indiceArvore.buscar(id);
    }

    /**
     * Busca a OS mais recente de uma placa usando TabelaHashOS — O(1).
     */
    public OrdemServicoModel buscarPorPlacaExata(String placa) {
        return indiceHash.buscar(placa);
    }

    // Processamento FIFO
    public OrdemServicoModel processarProximaOS() {
        if (filaEspera.estaVazia()) {
            return null;
        }
        return filaEspera.desenfileirar();
    }

    // --- Ordenação e agrupamento ---
    @Override
    public List<OrdemServicoModel> listarOrdenadoPorIdAsc() {
        return ordenadorPorId.ordenar(filaEspera, OrdenadorOS.Direcao.ASC);
    }

    @Override
    public List<OrdemServicoModel> listarOrdenadoPorIdDesc() {
        return ordenadorPorId.ordenar(filaEspera, OrdenadorOS.Direcao.DESC);
    }

    @Override
    public List<OrdemServicoModel> listarOrdenadoPorNomeClienteAsc() {
        return ordenadorPorNomeCliente.ordenar(filaEspera, OrdenadorOS.Direcao.ASC);
    }

    @Override
    public List<OrdemServicoModel> listarOrdenadoPorNomeClienteDesc() {
        return ordenadorPorNomeCliente.ordenar(filaEspera, OrdenadorOS.Direcao.DESC);
    }

    @Override
    public Map<OrdemServicoModel.StatusOrdemServico, List<OrdemServicoModel>> listarAgrupadoPorStatus() {
        return ordenadorPorId.agruparPorStatus(filaEspera);
    }

    /**
     * Busca todas as OS ativas do banco e enriquece cada uma com placaVeiculo e
     * nomeCliente nos campos @Transient.
     *
     * Nota: este método opera sobre o banco — não usa a FilaOS. A FilaOS é uma
     * estrutura em memória populada no afterInsert; ao reiniciar a aplicação
     * ela começa vazia, independente do banco.
     */
    @Override
    public List<OrdemServicoModel> findAllActiveEnriquecido() {
        List<OrdemServicoModel> lista = repository.findAllByAtivoTrue();

        for (OrdemServicoModel os : lista) {
            // Enriquece com placa do veículo
            if (os.getIdVeiculo() != null) {
                Optional<VeiculoModel> veiculo = veiculoRepository.findByIdAndAtivoTrue(os.getIdVeiculo());
                veiculo.ifPresent(v -> os.setPlacaVeiculo(v.getPlaca()));

                // Enriquece com nome do cliente via veículo
                veiculo.ifPresent(v -> {
                    if (v.getIdCliente() != null) {
                        Optional<ClienteModel> cliente = clienteRepository.findByIdAndAtivoTrue(v.getIdCliente());
                        cliente.ifPresent(c -> os.setNomeCliente(c.getNomeCompleto()));
                    }
                });
            }
        }

        return lista;
    }

    /**
     * Busca TODAS as OS de uma placa percorrendo a FilaOS.
     * Necessário porque TabelaHashOS guarda apenas a OS mais recente por placa.
     */
    @Override
    public List<OrdemServicoModel> buscarTodasPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return List.of();
        String normalizada = placa.toUpperCase().replace("-", "").trim();
        List<OrdemServicoModel> resultado = new java.util.ArrayList<>();
        for (OrdemServicoModel os : filaEspera) {
            if (os.getPlacaVeiculo() != null &&
                os.getPlacaVeiculo().toUpperCase().replace("-", "").trim().equals(normalizada)) {
                resultado.add(os);
            }
        }
        return resultado;
    }

    /**
     * Busca TODAS as OS de uma data percorrendo a FilaOS.
     */
    @Override
    public List<OrdemServicoModel> buscarTodasPorData(java.time.LocalDate data) {
        if (data == null) return List.of();
        List<OrdemServicoModel> resultado = new java.util.ArrayList<>();
        for (OrdemServicoModel os : filaEspera) {
            if (data.equals(os.getDataAbertura())) {
                resultado.add(os);
            }
        }
        return resultado;
    }

}