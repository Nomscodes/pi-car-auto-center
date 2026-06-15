package br.com.picarauto.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.repository.IClienteRepository;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.repository.IVeiculoRepository;
import br.com.picarauto.util.ArvoreOS;
import br.com.picarauto.util.FilaOS;
import br.com.picarauto.util.OrdenadorOS;
import br.com.picarauto.util.OrdenadorPorId;
import br.com.picarauto.util.OrdenadorPorNomeCliente;
import br.com.picarauto.util.TabelaHashOS;
import br.com.picarauto.validation.IOrdemServicoValidation;

/**
 *
 * @author Caio4breu
 */
@Service
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    private final FilaOS filaEspera = new FilaOS();

    // Índices em memória — complementam a FilaOS com buscas mais eficientes.
    // ArvoreOS: busca por id em O(log n) em vez de busca sequencial O(n) na fila.
    // TabelaHashOS: lookup por placa exata em O(1) em vez de varredura da fila.
    private final ArvoreOS     indiceArvore = new ArvoreOS();
    private final TabelaHashOS indiceHash   = new TabelaHashOS();

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

    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        if (entity.getDataAbertura() == null) {
            entity.setDataAbertura(LocalDate.now());
        }
    }

    /**
     * Sincroniza FilaOS, ArvoreOS e TabelaHashOS com o banco na inicialização.
     *
     * Problema resolvido: as três estruturas vivem em memória. Sem este método,
     * elas começam vazias a cada reinício da aplicação, mesmo que o banco já tenha
     * centenas de OS. Qualquer busca por id, placa ou ordenação retornaria vazio.
     *
     * Por que @PostConstruct e não no construtor?
     * No construtor o Spring ainda não injetou os repositórios — chamar
     * findAllActiveEnriquecido() ali causaria NullPointerException.
     * O @PostConstruct roda depois que toda a injeção de dependência está completa.
     *
     * Estrutura de Dados: a ordem de inserção na fila segue o id crescente
     * (ordem de criação no banco), preservando o comportamento FIFO esperado.
     */
    @PostConstruct
    public void sincronizarEstruturasComBanco() {
        List<OrdemServicoModel> ativas = findAllActiveEnriquecido();

        // Ordena por id para preservar a ordem FIFO original de chegada na oficina
        ativas.stream()
              .sorted(java.util.Comparator.comparingLong(os -> os.getId() != null ? os.getId() : 0L))
              .forEach(os -> {
                  filaEspera.enfileirar(os);  // Estrutura: Fila encadeada — ordem de atendimento
                  indiceArvore.inserir(os);   // Estrutura: BST — índice de busca por id O(log n)
                  indiceHash.inserir(os);     // Estrutura: Hash — índice de busca por placa O(1)
              });
    }

    // Enfileiramento e indexação após cada novo insert
    @Override
    protected void afterInsert(OrdemServicoModel savedEntity, OrdemServicoModel old) {
        filaEspera.enfileirar(savedEntity);  // entra no fim da fila (FIFO)
        indiceArvore.inserir(savedEntity);   // indexado na BST pelo id
        indiceHash.inserir(savedEntity);     // indexado na hash pela placa
    }

    // Consulta da fila
    public FilaOS getFilaEspera() {
        return filaEspera;
    }

    // Acesso aos índices em memória
    public ArvoreOS getIndiceArvore() {
        return indiceArvore;
    }

    public TabelaHashOS getIndiceHash() {
        return indiceHash;
    }

    /**
     * Busca uma OS por id usando a ArvoreOS (O(log n)).
     * Preferível à busca sequencial da FilaOS para consultas diretas por id.
     */
    public OrdemServicoModel buscarPorId(Long id) {
        return indiceArvore.buscar(id);
    }

    /**
     * Busca a OS mais recente de uma placa usando a TabelaHashOS (O(1)).
     * Preferível ao buscarPorPlaca() da FilaOS para lookup de placa exata.
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
     * Nota: este método opera diretamente sobre o banco — não usa a FilaOS nem os
     * índices em memória. É usado pelo @PostConstruct para popular as estruturas
     * na inicialização, e pela PanelListaOS para exibir dados sempre atualizados.
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
}