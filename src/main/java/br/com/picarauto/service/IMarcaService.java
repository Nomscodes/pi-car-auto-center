/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.repository.IMarcaRepository;
import br.com.picarauto.validation.IMarcaValidation;

/**
 *
 * @author Gabriel
 */
public interface IMarcaService extends IGenericService<MarcaModel, IMarcaRepository, IMarcaValidation> {
    
}
