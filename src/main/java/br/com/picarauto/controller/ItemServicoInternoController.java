package br.com.picarauto.controller;

import br.com.picarauto.factory.IServicoItemFactory;
import br.com.picarauto.factory.ServicoInternoFactory;
import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.service.IItemServicoInternoService;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *
 * @author Gabriel
 */

@Component
public class ItemServicoInternoController  extends GenericController<ItemServicoInternoModel, IItemServicoInternoService> {

    // Padrão de Projeto: Factory Method
    private final IServicoItemFactory itemFactory = new ServicoInternoFactory();

    public ItemServicoInternoController(IItemServicoInternoService service) {
        super(service);
    }

    public List<ItemServicoInternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }

    public ItemServicoInternoModel novoItem() {
        return (ItemServicoInternoModel) itemFactory.criar();
    }
}