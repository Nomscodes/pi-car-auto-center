package br.com.picarauto.service;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.repository.IPecaRepository;
import br.com.picarauto.validation.IPecaValidation;

/**
 *
 * @author Caio4breu
 */
public interface IPecaService extends IGenericService<PecaModel, IPecaRepository, IPecaValidation> {

    PecaModel findByCodigoNacional(Integer codigoNacional);
}