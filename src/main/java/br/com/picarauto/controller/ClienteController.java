package br.com.picarauto.controller;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.service.IClienteService;

public class ClienteController extends GenericController<ClienteModel, IClienteService> {
    public ClienteController(IClienteService service) {
        super(service);
    }
}