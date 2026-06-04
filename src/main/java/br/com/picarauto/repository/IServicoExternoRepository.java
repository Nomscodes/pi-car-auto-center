package br.com.picarauto.repository;
 
import br.com.picarauto.model.ServicoExternoModel;
 
/**
 *
 * @author Caio4breu
 */
public interface IServicoExternoRepository extends IGenericRepository<ServicoExternoModel> {
    boolean existsByDescricao(String descricao);
}