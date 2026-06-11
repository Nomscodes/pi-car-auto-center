/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.repository.IServicoInternoRepository;
import br.com.picarauto.validation.IServicoInternoValidation;

/**
 *
 * @author Gabriel
 */

public interface IServicoInternoService extends IGenericService<ServicoInternoModel, IServicoInternoRepository, IServicoInternoValidation>{
    
}
