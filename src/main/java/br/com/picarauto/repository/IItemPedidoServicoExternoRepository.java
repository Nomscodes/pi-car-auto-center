package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemPedidoServicoExternoRepository extends IGenericRepository<ItemPedidoServicoExternoModel> {

    List<ItemPedidoServicoExternoModel> findAllByIdServicoExterno(Long idServicoExterno);
    List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS);
}
