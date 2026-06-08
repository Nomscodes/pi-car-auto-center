package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.FuncaoColaboradorModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IFuncaoColaboradorRepository extends IGenericRepository<FuncaoColaboradorModel> {
    boolean existsByFuncao(String funcao);
}