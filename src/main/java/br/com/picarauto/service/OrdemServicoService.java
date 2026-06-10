package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.util.FilaOS;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    private final FilaOS filaEspera = new FilaOS();

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
}
