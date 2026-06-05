package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoInternoModel;

/**
 *
 * @author Caio4breu
 */
public interface IServicoInternoRepository extends IGenericRepository<ServicoInternoModel> {
    boolean existsByDescricao(String descricao);
}