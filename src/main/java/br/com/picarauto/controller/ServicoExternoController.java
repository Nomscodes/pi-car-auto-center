/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.service.IServicoExternoService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class ServicoExternoController extends GenericController<ServicoExternoModel, IServicoExternoService> {

    public ServicoExternoController(IServicoExternoService service) {
        super(service);
    }
}
