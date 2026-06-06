package br.com.picarauto.repository;

import br.com.picarauto.model.HistoricoVeiculoModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para HistoricoVeiculoModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IHistoricoVeiculoRepository extends IGenericRepository<HistoricoVeiculoModel> {
}
