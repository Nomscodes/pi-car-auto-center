package br.com.picarauto.controller;

import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.service.IPessoaFisicaService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class PessoaFisicaController extends GenericController<PessoaFisicaModel, IPessoaFisicaService> {

    public PessoaFisicaController(IPessoaFisicaService service) {
        super(service);
    }
}