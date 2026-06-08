package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ItemPedidoPecaModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemPedidoPecaRepository extends IGenericRepository<ItemPedidoPecaModel> {
    List<ItemPedidoPecaModel> findAllByIdOS(Long idOS);
    List<ItemPedidoPecaModel> findAllByCodigoNacional(Integer codigoNacional);
}