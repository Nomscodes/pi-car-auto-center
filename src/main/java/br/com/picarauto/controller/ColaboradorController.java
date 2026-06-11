/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.service.IColaboradorService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ColaboradorController extends GenericController<ColaboradorModel, IColaboradorService> {

    public ColaboradorController(IColaboradorService service) {
        super(service);
    }
}
