package br.com.picarauto.repository;

import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IItemPedidoServicoExternoRepository extends IGenericRepository<ItemPedidoServicoExternoModel> {
    // Spring Data navega pelo relacionamento @ManyToOne ServicoExternoModel
    List<ItemPedidoServicoExternoModel> findAllByServicoExternoId(Integer idServicoExterno);
}
