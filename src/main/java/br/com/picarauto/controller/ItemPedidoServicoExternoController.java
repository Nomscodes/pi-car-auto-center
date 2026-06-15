package br.com.picarauto.controller;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.service.IItemPedidoServicoExternoService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Component
public class ItemPedidoServicoExternoController
        extends GenericController<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoService> {

    public ItemPedidoServicoExternoController(IItemPedidoServicoExternoService service) {
        super(service);
    }

    /** Retorna todos os itens de serviço externo vinculados a uma OS. */
    public List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }
}