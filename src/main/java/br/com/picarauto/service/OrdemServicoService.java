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

    // Enfileiramento (afterInsert)
    @Override
    protected void afterInsert(OrdemServicoModel savedEntity, OrdemServicoModel old) {
        filaEspera.enfileirar(savedEntity);
    }

    // Consulta da fila
    public FilaOS getFilaEspera() {
        return filaEspera;
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
}
