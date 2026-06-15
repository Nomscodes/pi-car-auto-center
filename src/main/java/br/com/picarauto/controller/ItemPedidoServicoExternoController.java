package br.com.picarauto.controller;

import br.com.picarauto.factory.IServicoItemFactory;
import br.com.picarauto.factory.ServicoExternoFactory;
import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.service.IItemPedidoServicoExternoService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *
 * @author Gabriel
 */

@Component
public class ItemPedidoServicoExternoController extends GenericController<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoService> {

    // Padrão de Projeto: Factory Method
    private final IServicoItemFactory itemFactory = new ServicoExternoFactory();

    public ItemPedidoServicoExternoController(IItemPedidoServicoExternoService service) {
        super(service);
    }

    public List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }

    public ItemPedidoServicoExternoModel novoItem() {
        return (ItemPedidoServicoExternoModel) itemFactory.criar();
    }
}