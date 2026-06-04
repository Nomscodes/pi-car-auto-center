package br.com.picarauto.repository;

import br.com.picarauto.model.ItemServicoInternoModel;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public interface IItemServicoInternoRepository extends IGenericRepository<ItemServicoInternoModel> {
    List<ItemServicoInternoModel> findAllByIdOS(Integer idOS);
}