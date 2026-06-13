package br.com.picarauto.controller;

import br.com.picarauto.model.PessoaJuridicaModel;
import br.com.picarauto.service.IPessoaJuridicaService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class PessoaJuridicaController extends GenericController<PessoaJuridicaModel, IPessoaJuridicaService> {

    public PessoaJuridicaController(IPessoaJuridicaService service) {
        super(service);
    }
}