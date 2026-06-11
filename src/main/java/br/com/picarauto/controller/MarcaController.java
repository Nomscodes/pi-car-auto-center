/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.service.IMarcaService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class MarcaController extends GenericController<MarcaModel, IMarcaService> {

    public MarcaController(IMarcaService service) {
        super(service);
    }
}
