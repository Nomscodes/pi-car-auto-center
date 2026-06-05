package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.util.FilaOS;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.time.LocalDate;

/**
 *
 * @author Caio4breu
 */
public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    private final FilaOS filaEspera = new FilaOS();

    public OrdemServicoService(IOrdemServicoRepository repository, IOrdemServicoValidation validation) {
        super(repository, validation);
    }

    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        if (entity.getDataAbertura() == null)
            entity.setDataAbertura(LocalDate.now());
        filaEspera.enfileirar(entity);
    }
}