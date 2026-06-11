package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ServicoInternoModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicoInternoRepository extends IGenericRepository<ServicoInternoModel> {
    boolean existsByDescricao(String descricao);
}