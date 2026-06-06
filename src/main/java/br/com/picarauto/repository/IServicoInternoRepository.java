package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoInternoModel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IServicoInternoRepository extends IGenericRepository<ServicoInternoModel> {
    boolean existsByDescricao(String descricao);
}