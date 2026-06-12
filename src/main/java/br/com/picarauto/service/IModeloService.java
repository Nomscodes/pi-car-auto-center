/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import java.util.List;

import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.repository.IModeloRepository;
import br.com.picarauto.validation.IModeloValidation;

/**
 *
 * @author Gabriel
 */
public interface IModeloService extends IGenericService<ModeloModel, IModeloRepository, IModeloValidation> {

    List<ModeloModel> findAllByIdMarca(Long idMarca);
}
