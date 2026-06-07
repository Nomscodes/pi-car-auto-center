package br.com.picarauto.repository;

import br.com.picarauto.model.ColaboradorModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para ColaboradorModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IColaboradorRepository extends IGenericRepository<ColaboradorModel> {

    boolean existsByEmaul(String email);
}
