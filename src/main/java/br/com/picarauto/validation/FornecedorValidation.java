package br.com.picarauto.validation;

/**
 *
 * @author Cassiano
 */
import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IFornecedorRepository;
import org.springframework.stereotype.Component;

@Component
public class FornecedorValidation extends GenericValidation<FornecedorModel, IFornecedorRepository>
        implements IFornecedorValidation {

    public FornecedorValidation(IFornecedorRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(FornecedorModel entity) {
        super.validateFields(entity);
        if (entity.getNomeFornecedor() == null || entity.getNomeFornecedor().isBlank())
            throw new FieldValidationException("nomeFornecedor",
                    "O nome do fornecedor é de preenchimento obrigatório.");
        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone",
                    "O telefone do fornecedor é de preenchimento obrigatório.");
        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email",
                    "O e-mail do fornecedor é de preenchimento obrigatório.");
    }

    @Override
    public void validateInsert(FornecedorModel entity) {
        if (entity.getCnpj() != null && !entity.getCnpj().isBlank()) {
            if (repository.existsByCnpj(entity.getCnpj()))
                throw new RuleValidationException("CNPJ Duplicado",
                        "Já existe um fornecedor cadastrado com esse CNPJ.");
        }
    }
}
