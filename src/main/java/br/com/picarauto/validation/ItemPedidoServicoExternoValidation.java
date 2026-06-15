/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.validation;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.repository.IItemPedidoServicoExternoRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ItemPedidoServicoExternoValidation extends GenericValidation<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoRepository>
        implements IItemPedidoServicoExternoValidation {

    public ItemPedidoServicoExternoValidation(IItemPedidoServicoExternoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ItemPedidoServicoExternoModel entity) {
        super.validateFields(entity);
        if (entity.getIdOS() == null)
            throw new FieldValidationException("idOS", "A OS vinculada ao item de serviço externo é obrigatória.");
        if (entity.getIdServicoExterno() == null)
            throw new FieldValidationException("idServicoExterno", "O serviço externo de referência é obrigatório.");
        if (entity.getValorItem() == null || entity.getValorItem() < 0)
            throw new FieldValidationException("valorItem", "O valor do item externo não pode ser nulo ou negativo.");
        if (entity.getGarantia() == null || entity.getGarantia() < 0)
            throw new FieldValidationException("garantia", "O prazo de garantia não pode ser nulo ou negativo.");
    }
}