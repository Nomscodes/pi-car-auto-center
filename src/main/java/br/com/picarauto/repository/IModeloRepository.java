package br.com.picarauto.repository;

import br.com.picarauto.model.ModeloModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para ModeloModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IModeloRepository extends IGenericRepository<ModeloModel> {
}
