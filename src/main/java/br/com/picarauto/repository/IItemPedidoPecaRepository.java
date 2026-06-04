package br.com.picarauto.repository;

import br.com.picarauto.model.ItemPedidoPecaModel;
import java.util.List;

/**
 * 
 * @author Caio4breu
 */
public interface IItemPedidoPecaRepository extends IGenericRepository<ItemPedidoPecaModel> {
    List<ItemPedidoPecaModel> findAllByIdOS(Integer idOS);
    List<ItemPedidoPecaModel> findAllByCodigoNacional(Integer codigoNacional);
}