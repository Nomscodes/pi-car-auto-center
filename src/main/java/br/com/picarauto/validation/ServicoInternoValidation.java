package br.com.picarauto.validation;

import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IServicoInternoRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class ServicoInternoValidation extends GenericValidation<ServicoInternoModel, IServicoInternoRepository>
        implements IServicoInternoValidation {

    public ServicoInternoValidation(IServicoInternoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ServicoInternoModel entity) {
        super.validateFields(entity);
        if (entity.getDescricao() == null || entity.getDescricao().isBlank())
            throw new FieldValidationException("descricao",
                    "A descrição do serviço interno é de preenchimento obrigatório.");
        if (entity.getValorCobrado() <= 0)
            throw new FieldValidationException("valorCobrado",
                    "O valor cobrado deve ser maior que zero.");
    }

    @Override
    public void validateInsert(ServicoInternoModel entity) {
        if (repository.existsByDescricao(entity.getDescricao()))
            throw new RuleValidationException("Descrição Duplicada",
                    "Já existe um serviço interno cadastrado com essa descrição.");
    }
}