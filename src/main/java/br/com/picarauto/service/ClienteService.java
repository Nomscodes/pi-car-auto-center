package br.com.picarauto.service;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.repository.IClienteRepository;
import br.com.picarauto.validation.IClienteValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class ClienteService extends GenericService<ClienteModel, IClienteRepository, IClienteValidation>
        implements IClienteService {

    @Autowired
    public ClienteService(IClienteRepository repository, IClienteValidation validation) {
        super(repository, validation);
    }
}