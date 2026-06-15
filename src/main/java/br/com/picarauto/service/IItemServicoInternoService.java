package br.com.picarauto.service;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import br.com.picarauto.validation.IItemServicoInternoValidation;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public interface IItemServicoInternoService
        extends IGenericService<ItemServicoInternoModel, IItemServicoInternoRepository, IItemServicoInternoValidation> {

    /** Retorna todos os itens de serviço interno ativos vinculados a uma OS. */
    List<ItemServicoInternoModel> findAllByIdOS(Long idOS);
}