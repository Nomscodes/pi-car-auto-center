package br.com.picarauto.service;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.repository.IMecanicoRepository;
import br.com.picarauto.validation.IMecanicoValidation;

public class MecanicoService extends GenericService<MecanicoModel, IMecanicoRepository, IMecanicoValidation>
        implements IMecanicoService {

    public MecanicoService(IMecanicoRepository repository, IMecanicoValidation validation) {
        super(repository, validation);
    }
}