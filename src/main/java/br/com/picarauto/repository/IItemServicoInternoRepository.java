package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ItemServicoInternoModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemServicoInternoRepository extends IGenericRepository<ItemServicoInternoModel> {
    List<ItemServicoInternoModel> findAllByIdOS(Long idOS);
}