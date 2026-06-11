package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.util.FilaOS;
import br.com.picarauto.util.OrdenadorOS;
import br.com.picarauto.util.OrdenadorPorId;
import br.com.picarauto.util.OrdenadorPorNomeCliente;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    private final FilaOS filaEspera = new FilaOS();

    // Instâncias únicas — os ordenadores são stateless, não precisam ser recriados a cada chamada.
    private final OrdenadorOS ordenadorPorId          = new OrdenadorPorId();
    private final OrdenadorOS ordenadorPorNomeCliente = new OrdenadorPorNomeCliente();

    public OrdemServicoService(IOrdemServicoRepository repository, IOrdemServicoValidation validation) {
        super(repository, validation);
    }

    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        if (entity.getDataAbertura() == null) {
            entity.setDataAbertura(LocalDate.now());
        }
    }

    //Enfileiramento (afterInsert)
    @Override
    protected void afterInsert(OrdemServicoModel savedEntity, OrdemServicoModel old) {
        filaEspera.enfileirar(savedEntity);
    }

    //Consulta da fila
    public FilaOS getFilaEspera() {
        return filaEspera;
    }

    //Processamento FIFO
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
        // Qualquer ordenador serve para agrupar — o agrupamento é definido no OrdenadorOS pai.
        // Usamos ordenadorPorId apenas como ponto de entrada para o método herdado.
        return ordenadorPorId.agruparPorStatus(filaEspera);
    }
}