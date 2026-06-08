package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PecaModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IPecaRepository extends IGenericRepository<PecaModel> {
    Optional<PecaModel> findByCodigoNacional(Integer codigoNacional);
    boolean existsByCodigoNacional(Integer codigoNacional);
}