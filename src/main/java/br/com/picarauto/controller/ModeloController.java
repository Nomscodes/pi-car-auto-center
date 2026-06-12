/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.service.IModeloService;

/**
 *
 * @author Gabriel
 */
@Component
public class ModeloController extends GenericController<ModeloModel, IModeloService> {

    public ModeloController(IModeloService service) {
        super(service);
    }

    public List<ModeloModel> findAllByIdMarca(Long idMarca) {
        return service.findAllByIdMarca(idMarca);
    }
}
