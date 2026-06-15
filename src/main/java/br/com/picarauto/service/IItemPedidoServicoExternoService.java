package br.com.picarauto.service;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.repository.IItemPedidoServicoExternoRepository;
import br.com.picarauto.validation.IItemPedidoServicoExternoValidation;
import java.util.List;

/**
 *
 * @authorCaio 4breu
 */
public interface IItemPedidoServicoExternoService
        extends IGenericService<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoRepository, IItemPedidoServicoExternoValidation> {

    /** Retorna todos os itens de serviço externo ativos vinculados a uma OS. */
    List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS);
}