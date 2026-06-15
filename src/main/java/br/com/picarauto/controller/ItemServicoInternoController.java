package br.com.picarauto.controller;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.service.IItemServicoInternoService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
@Component
public class ItemServicoInternoController
        extends GenericController<ItemServicoInternoModel, IItemServicoInternoService> {

    public ItemServicoInternoController(IItemServicoInternoService service) {
        super(service);
    }

    /** Retorna todos os itens de serviço interno vinculados a uma OS. */
    public List<ItemServicoInternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }
}