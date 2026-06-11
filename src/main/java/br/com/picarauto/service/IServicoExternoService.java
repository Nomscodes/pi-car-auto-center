/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.repository.IServicoExternoRepository;
import br.com.picarauto.validation.IServicoExternoValidation;

/**
 *
 * @author Gabriel
 */

public interface IServicoExternoService extends IGenericService<ServicoExternoModel, IServicoExternoRepository, IServicoExternoValidation>{
    
}
