/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

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

    public ItemServicoInternoController(IItemServicoInternoService service) {
        super(service);
    }

    public List<ItemServicoInternoModel> findAllByIdOS(Long idOS) {
        return service.findAllByIdOS(idOS);
    }
}
