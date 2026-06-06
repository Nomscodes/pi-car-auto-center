package br.com.picarauto.repository;

import br.com.picarauto.model.ItemPedidoPecaModel;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IItemPedidoPecaRepository extends IGenericRepository<ItemPedidoPecaModel> {
    // Busca todos os itens de peça de uma OS
    List<ItemPedidoPecaModel> findAllByOrdemServicoId(Integer idOS);

    // Busca todos os pedidos de uma peça pelo código nacional
    List<ItemPedidoPecaModel> findAllByPecaCodigoNacional(Integer codigoNacional);
}
