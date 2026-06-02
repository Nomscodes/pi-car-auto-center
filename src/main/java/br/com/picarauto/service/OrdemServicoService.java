package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.util.Date;

public class OrdemServicoService extends GenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation>
        implements IOrdemServicoService {

    public OrdemServicoService(IOrdemServicoRepository repository, IOrdemServicoValidation validation) {
        super(repository, validation);
    }

    @Override
    protected void beforeInsert(OrdemServicoModel entity) {
        entity.setNumero(repository.gerarProximoNumero());
        if (entity.getDataAbertura() == null)
            entity.setDataAbertura(new Date());
        if (entity.getDataEntrada() == null)
            entity.setDataEntrada(new Date());
    }
}