package br.com.picarauto.controller;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.service.IPecaService;
import org.springframework.stereotype.Component;

@Component
public class PecaController extends GenericController<PecaModel, IPecaService> {
    public PecaController(IPecaService service) {
        super(service);
    }

    /** Busca peça pelo código nacional — operação específica deste domínio. */
    public PecaModel findByCodigoNacional(Integer codigoNacional) {
        return service.findByCodigoNacional(codigoNacional);
    }
}