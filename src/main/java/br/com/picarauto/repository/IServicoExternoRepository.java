package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoExternoModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para ServicoExternoModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IServicoExternoRepository extends IGenericRepository<ServicoExternoModel> {
}
