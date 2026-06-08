package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ServicoExternoModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicoExternoRepository extends IGenericRepository<ServicoExternoModel> {
    boolean existsByDescricao(String descricao);
}