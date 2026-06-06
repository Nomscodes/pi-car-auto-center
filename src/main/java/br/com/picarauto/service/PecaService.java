package br.com.picarauto.service;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.repository.IPecaRepository;
import br.com.picarauto.validation.IPecaValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class PecaService extends GenericService<PecaModel, IPecaRepository, IPecaValidation>
        implements IPecaService {

    @Autowired
    public PecaService(IPecaRepository repository, IPecaValidation validation) {
        super(repository, validation);
    }

    @Override
    public PecaModel findByCodigoNacional(Integer codigoNacional) {
        return repository.findByCodigoNacional(codigoNacional)
                .orElseThrow(() -> new BusinessException(
                    "Peça não encontrada para o código: " + codigoNacional
                ));
    }
}
