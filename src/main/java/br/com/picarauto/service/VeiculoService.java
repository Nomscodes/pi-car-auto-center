package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.repository.IVeiculoRepository;
import br.com.picarauto.validation.IVeiculoValidation;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService extends GenericService<VeiculoModel, IVeiculoRepository, IVeiculoValidation>
        implements IVeiculoService {

    public VeiculoService(IVeiculoRepository repository, IVeiculoValidation validation) {
        super(repository, validation);
    }
}