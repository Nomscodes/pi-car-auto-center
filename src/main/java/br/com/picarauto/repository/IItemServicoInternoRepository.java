package br.com.picarauto.repository;

import br.com.picarauto.model.ItemServicoInternoModel;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IItemServicoInternoRepository extends IGenericRepository<ItemServicoInternoModel> {
    // Spring Data navega pelo relacionamento @ManyToOne para gerar o SQL
    List<ItemServicoInternoModel> findAllByOrdemServicoId(Integer idOS);
}
