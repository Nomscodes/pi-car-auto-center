/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.repository.IServicoExternoRepository;
import br.com.picarauto.validation.IServicoExternoValidation;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class ServicoExternoService extends GenericService<ServicoExternoModel, IServicoExternoRepository, IServicoExternoValidation> implements IServicoExternoService {

    public ServicoExternoService(IServicoExternoRepository repository, IServicoExternoValidation validation) {
        super(repository, validation);
    }   
}
