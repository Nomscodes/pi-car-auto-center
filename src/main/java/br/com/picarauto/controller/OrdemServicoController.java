package br.com.picarauto.controller;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.service.IOrdemServicoService;

public class OrdemServicoController extends GenericController<OrdemServicoModel, IOrdemServicoService> {
    public OrdemServicoController(IOrdemServicoService service) {
        super(service);
    }
}