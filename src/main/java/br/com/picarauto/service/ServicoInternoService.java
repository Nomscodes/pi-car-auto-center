package br.com.picarauto.service;

import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.repository.IServicoInternoRepository;
import br.com.picarauto.validation.IServicoInternoValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class ServicoInternoService extends GenericService<ServicoInternoModel, IServicoInternoRepository, IServicoInternoValidation> implements IServicoInternoService {

    public ServicoInternoService(IServicoInternoRepository repository, IServicoInternoValidation validation) {
        super(repository, validation);
    }
}