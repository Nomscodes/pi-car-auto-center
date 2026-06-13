package br.com.picarauto.service;

import br.com.picarauto.model.PessoaJuridicaModel;
import br.com.picarauto.repository.IPessoaJuridicaRepository;
import br.com.picarauto.validation.IPessoaJuridicaValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class PessoaJuridicaService extends GenericService<PessoaJuridicaModel, IPessoaJuridicaRepository, IPessoaJuridicaValidation>
        implements IPessoaJuridicaService {

    public PessoaJuridicaService(IPessoaJuridicaRepository repository, IPessoaJuridicaValidation validation) {
        super(repository, validation);
    }
}