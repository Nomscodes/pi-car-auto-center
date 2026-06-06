package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.util.FilaOS;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    // Fila de espera em memória — mantida durante toda a execução da aplicação.
    // Como o Spring garante uma única instância deste service (@Service = Singleton),
    // esta fila também é única e compartilhada por toda a aplicação.
    private final FilaOS filaEspera = new FilaOS();

    @Autowired
    public OrdemServicoService(IOrdemServicoRepository repository, IOrdemServicoValidation validation) {
        super(repository, validation);
    }

    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        if (entity.getDataAbertura() == null)
            entity.setDataAbertura(LocalDate.now());
        filaEspera.enfileirar(entity);
    }

    public FilaOS getFilaEspera() {
        return filaEspera;
    }
}
