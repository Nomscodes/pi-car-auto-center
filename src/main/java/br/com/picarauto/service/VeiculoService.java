package br.com.picarauto.service;

import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.repository.IVeiculoRepository;
import br.com.picarauto.validation.IVeiculoValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class VeiculoService extends GenericService<VeiculoModel, IVeiculoRepository, IVeiculoValidation>
        implements IVeiculoService {

    @Autowired
    public VeiculoService(IVeiculoRepository repository, IVeiculoValidation validation) {
        super(repository, validation);
    }
}
