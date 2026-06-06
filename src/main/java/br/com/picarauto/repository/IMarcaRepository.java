package br.com.picarauto.repository;

import br.com.picarauto.model.MarcaModel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Caio4breu
 */
@Repository
public interface IMarcaRepository extends IGenericRepository<MarcaModel> {
    boolean existsByNome(String nome);
}