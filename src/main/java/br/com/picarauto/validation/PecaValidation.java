package br.com.picarauto.validation;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IPecaRepository;
import java.math.BigDecimal;

public class PecaValidation extends GenericValidation<PecaModel, IPecaRepository>
        implements IPecaValidation {

    public PecaValidation(IPecaRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(PecaModel entity) {
        super.validateFields(entity);

        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome",
                    "O nome da peça é de preenchimento obrigatório.");

        if (entity.getQuantidade() == null)
            throw new FieldValidationException("quantidade",
                    "A quantidade da peça é de preenchimento obrigatório.");

        if (entity.getQuantidade() < 0)
            throw new FieldValidationException("quantidade",
                    "A quantidade não pode ser negativa.");

        if (entity.getValorUnitario() == null)
            throw new FieldValidationException("valorUnitario",
                    "O valor unitário da peça é de preenchimento obrigatório.");

        if (entity.getValorUnitario().compareTo(BigDecimal.ZERO) < 0)
            throw new FieldValidationException("valorUnitario",
                    "O valor unitário não pode ser negativo.");
    }

    @Override
    public void validateInsert(PecaModel entity) {
        if (repository.existsByNome(entity.getNome()))
            throw new RuleValidationException("Nome Duplicado",
                    "Já existe uma peça cadastrada com esse nome.");
    }
}