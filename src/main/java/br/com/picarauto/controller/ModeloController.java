/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.service.IModeloService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ModeloController extends GenericController<ModeloModel, IModeloService> {

    public ModeloController(IModeloService service) {
        super(service);
    }
}
