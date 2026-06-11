/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.controller;

import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.service.IFornecedorService;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */

@Component
public class FornecedorController extends GenericController<FornecedorModel, IFornecedorService> {

    public FornecedorController(IFornecedorService service) {
        super(service);
    }
}
