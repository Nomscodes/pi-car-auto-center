/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.repository.IItemPedidoServicoExternoRepository;
import br.com.picarauto.validation.IItemPedidoServicoExternoValidation;
import java.util.List;

/**
 *
 * @author Gabriel
 */

public interface IItemPedidoServicoExternoService extends IGenericService<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoRepository, IItemPedidoServicoExternoValidation> {

    List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS);
}
