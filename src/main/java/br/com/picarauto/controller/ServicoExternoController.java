package br.com.picarauto.controller;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.service.IServicoExternoService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ServicoExternoController extends GenericController<ServicoExternoModel, IServicoExternoService> {

    public ServicoExternoController(IServicoExternoService service) {
        super(service);
    }
}