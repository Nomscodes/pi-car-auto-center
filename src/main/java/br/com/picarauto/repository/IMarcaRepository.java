package br.com.picarauto.repository;

import br.com.picarauto.model.MarcaModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para MarcaModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IMarcaRepository extends IGenericRepository<MarcaModel> {
}
