package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoExternoModel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IServicoExternoRepository extends IGenericRepository<ServicoExternoModel> {
    boolean existsByDescricao(String descricao);
}