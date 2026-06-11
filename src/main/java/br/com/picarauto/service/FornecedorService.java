/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.repository.IFornecedorRepository;
import br.com.picarauto.validation.IFornecedorValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class FornecedorService extends GenericService<FornecedorModel, IFornecedorRepository, IFornecedorValidation> implements IFornecedorService {

    public FornecedorService(IFornecedorRepository repository, IFornecedorValidation validation) {
        super(repository, validation);
    }
}
