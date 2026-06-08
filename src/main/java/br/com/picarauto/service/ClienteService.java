package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.repository.IClienteRepository;
import br.com.picarauto.validation.IClienteValidation;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends GenericService<ClienteModel, IClienteRepository, IClienteValidation>
        implements IClienteService {
    public ClienteService(IClienteRepository repository, IClienteValidation validation) {
        super(repository, validation);
    }
}