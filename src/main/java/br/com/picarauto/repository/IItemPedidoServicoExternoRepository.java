package br.com.picarauto.repository;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public interface IItemPedidoServicoExternoRepository extends IGenericRepository<ItemPedidoServicoExternoModel> {
    List<ItemPedidoServicoExternoModel> findAllByIdServicoExterno(Integer idServicoExterno);
}