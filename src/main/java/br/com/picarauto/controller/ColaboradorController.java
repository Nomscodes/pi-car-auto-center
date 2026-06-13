package br.com.picarauto.controller;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.service.IColaboradorService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ColaboradorController extends GenericController<ColaboradorModel, IColaboradorService> {

    public ColaboradorController(IColaboradorService service) {
        super(service);
    }
}