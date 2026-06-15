package br.com.picarauto.service;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.repository.IItemPedidoServicoExternoRepository;
import br.com.picarauto.validation.IItemPedidoServicoExternoValidation;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Service
public class ItemPedidoServicoExternoService
        extends GenericService<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoRepository, IItemPedidoServicoExternoValidation>
        implements IItemPedidoServicoExternoService {

    public ItemPedidoServicoExternoService(IItemPedidoServicoExternoRepository repository,
                                           IItemPedidoServicoExternoValidation validation) {
        super(repository, validation);
    }

    @Override
    public List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS) {
        return repository.findAllByIdOS(idOS);
    }
}