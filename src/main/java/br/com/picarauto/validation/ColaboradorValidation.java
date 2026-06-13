package br.com.picarauto.validation;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IColaboradorRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */
@Component
public class ColaboradorValidation extends GenericValidation<ColaboradorModel, IColaboradorRepository>
        implements IColaboradorValidation {

    public ColaboradorValidation(IColaboradorRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ColaboradorModel entity) {
        super.validateFields(entity);

        if (entity.getNomeCompleto() == null || entity.getNomeCompleto().isBlank())
            throw new FieldValidationException("nomeCompleto", "O nome completo é obrigatório.");

        if (entity.getCpf() == null || entity.getCpf().isBlank())
            throw new FieldValidationException("cpf", "O CPF é obrigatório.");
        String cpfLimpo = entity.getCpf().replaceAll("\\D", "");
        if (cpfLimpo.length() != 11)
            throw new FieldValidationException("cpf", "O CPF deve ter 11 dígitos.");
        entity.setCpf(cpfLimpo);

        if (entity.getDataAdmissao() == null)
            throw new FieldValidationException("dataAdmissao", "A data de admissão é obrigatória.");

        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone", "O telefone é obrigatório.");

        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email", "O e-mail é obrigatório.");
        if (!entity.getEmail().matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$"))
            throw new FieldValidationException("email", "O e-mail informado não é válido.");

        if (entity.getEndereco() == null || entity.getEndereco().isBlank())
            throw new FieldValidationException("endereco", "O endereço é obrigatório.");

        if (entity.getSalario() <= 0)
            throw new FieldValidationException("salario", "O salário deve ser maior que zero.");
    }

    @Override
    public void validateInsert(ColaboradorModel entity) {
        if (repository.existsByCpf(entity.getCpf()))
            throw new RuleValidationException("cpf", "Já existe um colaborador com esse CPF.");
    }
}