package br.com.picarauto.controller;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.service.IVeiculoService;
import org.springframework.stereotype.Component;

@Component
public class VeiculoController extends GenericController<VeiculoModel, IVeiculoService> {
    public VeiculoController(IVeiculoService service) {
        super(service);
    }
}