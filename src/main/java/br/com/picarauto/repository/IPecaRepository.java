package br.com.picarauto.repository;

import br.com.picarauto.model.PecaModel;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório Spring Data para PecaModel.
 */
@Repository
public interface IPecaRepository extends IGenericRepository<PecaModel> {
    Optional<PecaModel> findByCodigoNacional(Integer codigoNacional);
    boolean existsByCodigoNacional(Integer codigoNacional);
}
