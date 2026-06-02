package br.com.picarauto.service;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.repository.IPecaRepository;
import br.com.picarauto.validation.IPecaValidation;

public class PecaService extends GenericService<PecaModel, IPecaRepository, IPecaValidation>
        implements IPecaService {

    public PecaService(IPecaRepository repository, IPecaValidation validation) {
        super(repository, validation);
    }
}