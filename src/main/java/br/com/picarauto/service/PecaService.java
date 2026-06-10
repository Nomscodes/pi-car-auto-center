package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.repository.IPecaRepository;
import br.com.picarauto.validation.IPecaValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; //import adicionado

@Service
public class PecaService extends GenericService<PecaModel, IPecaRepository, IPecaValidation>
        implements IPecaService {

    public PecaService(IPecaRepository repository, IPecaValidation validation) {
        super(repository, validation);
    }

    @Override
    @Transactional(readOnly = true) //Anotação adicionada para garantir o contexto transacional
    public PecaModel findByCodigoNacional(Integer codigoNacional) {
        return repository.findByCodigoNacional(codigoNacional)
                .orElseThrow(() -> new BusinessException("Peça não encontrada para o código: " + codigoNacional));
    }
}