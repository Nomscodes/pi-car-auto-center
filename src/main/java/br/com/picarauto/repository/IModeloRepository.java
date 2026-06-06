package br.com.picarauto.repository;

import br.com.picarauto.model.ModeloModel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IModeloRepository extends IGenericRepository<ModeloModel> {
    boolean existsByNomeModelo(String nomeModelo);
}