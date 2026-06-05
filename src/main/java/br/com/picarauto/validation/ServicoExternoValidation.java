package br.com.picarauto.validation;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IServicoExternoRepository;

/**
 *
 * @author Caio4breu
 */
public class ServicoExternoValidation extends GenericValidation<ServicoExternoModel, IServicoExternoRepository>
        implements IServicoExternoValidation {

    public ServicoExternoValidation(IServicoExternoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ServicoExternoModel entity) {
        super.validateFields(entity);

        if (entity.getDescricao() == null || entity.getDescricao().isBlank())
            throw new FieldValidationException("descricao",
                    "A descrição do serviço externo é de preenchimento obrigatório.");

        if (entity.getValorCobrado() <= 0)
            throw new FieldValidationException("valorCobrado",
                    "O valor cobrado deve ser maior que zero.");
    }

    @Override
    public void validateInsert(ServicoExternoModel entity) {
        if (repository.existsByDescricao(entity.getDescricao()))
            throw new RuleValidationException("Descrição Duplicada",
                    "Já existe um serviço externo cadastrado com essa descrição.");
    }
}