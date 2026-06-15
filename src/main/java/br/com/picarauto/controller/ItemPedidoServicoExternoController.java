/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

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

    public ItemPedidoServicoExternoController(IItemPedidoServicoExternoService service) {
        super(service);
    }

    public List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }
}