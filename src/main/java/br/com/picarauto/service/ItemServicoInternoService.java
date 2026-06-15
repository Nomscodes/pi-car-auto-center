/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import br.com.picarauto.validation.IItemServicoInternoValidation;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Gabriel
 */

@Service
public class ItemServicoInternoService  extends GenericService<ItemServicoInternoModel, IItemServicoInternoRepository, IItemServicoInternoValidation>
        implements IItemServicoInternoService {

    public ItemServicoInternoService(IItemServicoInternoRepository repository,
                                     IItemServicoInternoValidation validation) {
        super(repository, validation);
    }

    @Override
    public List<ItemServicoInternoModel> findAllByIdOS(Long idOS) {
        return repository.findAllByIdOS(idOS);
    }
}
