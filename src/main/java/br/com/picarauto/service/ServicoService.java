package br.com.picarauto.service;

import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.repository.IServicoRepository;
import br.com.picarauto.validation.IServicoValidation;

public class ServicoService extends GenericService<ServicoModel, IServicoRepository, IServicoValidation>
        implements IServicoService {

    public ServicoService(IServicoRepository repository, IServicoValidation validation) {
        super(repository, validation);
    }
}