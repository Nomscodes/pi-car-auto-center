/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.service.IServicoInternoService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ServicoInternoController extends GenericController<ServicoInternoModel, IServicoInternoService> {

    public ServicoInternoController(IServicoInternoService service) {
        super(service);
    }
}
