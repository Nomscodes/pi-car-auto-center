/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.repository.IMarcaRepository;
import br.com.picarauto.validation.IMarcaValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class MarcaService  extends GenericService<MarcaModel, IMarcaRepository, IMarcaValidation> implements IMarcaService{
   
    public MarcaService(IMarcaRepository repository, IMarcaValidation validation) {
        super(repository, validation);
    }
}
