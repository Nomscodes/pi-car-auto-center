/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import br.com.picarauto.validation.IItemServicoInternoValidation;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public interface IItemServicoInternoService extends IGenericService<ItemServicoInternoModel, IItemServicoInternoRepository, IItemServicoInternoValidation> {

    List<ItemServicoInternoModel> findAllByIdOS(Long idOS);
}