package br.com.picarauto.repository;

import br.com.picarauto.model.PecaModel;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public interface IPecaRepository extends IGenericRepository<PecaModel> {

    PecaModel findByCodigoNacional(Integer codigoNacional);
    List<PecaModel> findAllByAtivoTrue();
    boolean existsByCodigoNacional(Integer codigoNacional);
}