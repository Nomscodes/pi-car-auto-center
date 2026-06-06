package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoInternoModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para ServicoInternoModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IServicoInternoRepository extends IGenericRepository<ServicoInternoModel> {
}
