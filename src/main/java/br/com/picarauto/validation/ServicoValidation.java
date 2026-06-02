package br.com.picarauto.validation;

import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IServicoRepository;
import java.math.BigDecimal;

public class ServicoValidation extends GenericValidation<ServicoModel, IServicoRepository>
        implements IServicoValidation {

    public ServicoValidation(IServicoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ServicoModel entity) {
        super.validateFields(entity);
        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome", "O nome do serviço é de preenchimento obrigatório.");
        if (entity.getDescricao() == null || entity.getDescricao().isBlank())
            throw new FieldValidationException("descricao", "A descrição do serviço é de preenchimento obrigatório.");
        if (entity.getPreco() == null)
            throw new FieldValidationException("preco", "O preço do serviço é de preenchimento obrigatório.");
        if (entity.getPreco().compareTo(BigDecimal.ZERO) < 0)
            throw new FieldValidationException("preco", "O preço não pode ser negativo.");
    }

    @Override
    public void validateInsert(ServicoModel entity) {
        if (repository.existsByNome(entity.getNome()))
            throw new RuleValidationException("Nome Duplicado", "Já existe um serviço cadastrado com esse nome.");
    }
}