package br.com.picarauto.service;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.repository.IPecaRepository;
import br.com.picarauto.validation.IPecaValidation;

/**
 *
 * @author Caio4breu
 */
public class PecaService extends GenericService<PecaModel, IPecaRepository, IPecaValidation>
        implements IPecaService {

    public PecaService(IPecaRepository repository, IPecaValidation validation) {
        super(repository, validation);
    }

    @Override
    public PecaModel findByCodigoNacional(Integer codigoNacional) {
        PecaModel peca = repository.findByCodigoNacional(codigoNacional);
        if (peca == null)
            throw new BusinessException("Peça não encontrada para o código: " + codigoNacional);
        return peca;
    }
}