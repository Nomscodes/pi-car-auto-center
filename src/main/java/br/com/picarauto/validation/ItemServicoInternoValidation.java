package br.com.picarauto.validation;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class ItemServicoInternoValidation
        extends GenericValidation<ItemServicoInternoModel, IItemServicoInternoRepository>
        implements IItemServicoInternoValidation {

    public ItemServicoInternoValidation(IItemServicoInternoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ItemServicoInternoModel entity) {
        super.validateFields(entity);
        if (entity.getIdOS() == null)
            throw new FieldValidationException("idOS", "A OS vinculada ao item de serviço é obrigatória.");
        if (entity.getObservacoes() == null || entity.getObservacoes().isBlank())
            throw new FieldValidationException("observacoes", "A descrição do serviço é obrigatória.");
        if (entity.getValorItem() < 0)
            throw new FieldValidationException("valorItem", "O valor do item não pode ser negativo.");
        if (entity.getGarantia() < 0)
            throw new FieldValidationException("garantia", "O prazo de garantia não pode ser negativo.");
    }
}