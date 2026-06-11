/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.repository.IModeloRepository;
import br.com.picarauto.validation.IModeloValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class ModeloService extends GenericService<ModeloModel, IModeloRepository, IModeloValidation> implements IModeloService {

    public ModeloService(IModeloRepository repository, IModeloValidation validation) {
        super(repository, validation);
    }
}
