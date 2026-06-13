package br.com.picarauto.service;

import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.repository.IPessoaFisicaRepository;
import br.com.picarauto.validation.IPessoaFisicaValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class PessoaFisicaService extends GenericService<PessoaFisicaModel, IPessoaFisicaRepository, IPessoaFisicaValidation>
        implements IPessoaFisicaService {

    public PessoaFisicaService(IPessoaFisicaRepository repository, IPessoaFisicaValidation validation) {
        super(repository, validation);
    }
}