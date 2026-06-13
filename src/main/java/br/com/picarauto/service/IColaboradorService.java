package br.com.picarauto.service;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.repository.IColaboradorRepository;
import br.com.picarauto.validation.IColaboradorValidation;

/**
 *
 * @author Gabriel
 */

public interface IColaboradorService 
        extends IGenericService<ColaboradorModel, 
                IColaboradorRepository, 
                IColaboradorValidation> {}
