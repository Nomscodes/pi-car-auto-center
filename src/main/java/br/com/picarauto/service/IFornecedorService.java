/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.repository.IFornecedorRepository;
import br.com.picarauto.validation.IFornecedorValidation;

/**
 *
 * @author Gabriel
 */

public interface IFornecedorService extends IGenericService<FornecedorModel, IFornecedorRepository, IFornecedorValidation>{
    
}
