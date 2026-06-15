package br.com.picarauto.service;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import br.com.picarauto.validation.IItemServicoInternoValidation;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Service
public class ItemServicoInternoService
        extends GenericService<ItemServicoInternoModel, IItemServicoInternoRepository, IItemServicoInternoValidation>
        implements IItemServicoInternoService {

    public ItemServicoInternoService(IItemServicoInternoRepository repository,
                                     IItemServicoInternoValidation validation) {
        super(repository, validation);
    }

    @Override
    public List<ItemServicoInternoModel> findAllByIdOS(Long idOS) {
        return repository.findAllByIdOS(idOS);
    }
}