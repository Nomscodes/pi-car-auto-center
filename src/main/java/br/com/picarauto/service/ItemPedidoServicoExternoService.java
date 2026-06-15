/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.service;
import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.repository.IItemPedidoServicoExternoRepository;
import br.com.picarauto.validation.IItemPedidoServicoExternoValidation;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author Gabriel
 */

@Service
public class ItemPedidoServicoExternoService  extends GenericService<ItemPedidoServicoExternoModel, IItemPedidoServicoExternoRepository, IItemPedidoServicoExternoValidation>
        implements IItemPedidoServicoExternoService {

    public ItemPedidoServicoExternoService(IItemPedidoServicoExternoRepository repository,
                                           IItemPedidoServicoExternoValidation validation) {
        super(repository, validation);
    }

    @Override
    public List<ItemPedidoServicoExternoModel> findAllByIdOS(Long idOS) {
        return repository.findAllByIdOS(idOS);
    }
}
