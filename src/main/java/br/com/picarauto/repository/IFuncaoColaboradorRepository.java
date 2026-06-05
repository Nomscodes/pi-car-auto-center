package br.com.picarauto.repository;

import br.com.picarauto.model.FuncaoColaboradorModel;

/**
 *
 * @author Caio4breu
 */
public interface IFuncaoColaboradorRepository extends IGenericRepository<FuncaoColaboradorModel> {
    boolean existsByFuncao(String funcao);
}