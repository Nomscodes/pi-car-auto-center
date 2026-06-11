/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.repository.IColaboradorRepository;
import br.com.picarauto.validation.IColaboradorValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class ColaboradorService extends GenericService<ColaboradorModel, IColaboradorRepository, IColaboradorValidation> implements IColaboradorService {

    public ColaboradorService(IColaboradorRepository repository, IColaboradorValidation validation) {
        super(repository, validation);
    }
}
