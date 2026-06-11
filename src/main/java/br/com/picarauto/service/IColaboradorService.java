/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.repository.IColaboradorRepository;
import br.com.picarauto.validation.IColaboradorValidation;

/**
 *
 * @author Gabriel
 */

public interface IColaboradorService extends IGenericService<ColaboradorModel, IColaboradorRepository, IColaboradorValidation>  {
    
}
