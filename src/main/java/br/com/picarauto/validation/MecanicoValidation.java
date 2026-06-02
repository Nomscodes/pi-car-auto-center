package br.com.picarauto.validation;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IMecanicoRepository;

public class MecanicoValidation extends GenericValidation<MecanicoModel, IMecanicoRepository>
        implements IMecanicoValidation {

    public MecanicoValidation(IMecanicoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(MecanicoModel entity) {
        super.validateFields(entity);
        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome", "O nome do mecânico é de preenchimento obrigatório.");
        if (entity.getCpf() == null || entity.getCpf().isBlank())
            throw new FieldValidationException("cpf", "O CPF do mecânico é de preenchimento obrigatório.");
        if (entity.getCpf().replaceAll("[^0-9]", "").length() != 11)
            throw new FieldValidationException("cpf", "O CPF deve conter 11 dígitos numéricos.");
        if (entity.getEspecialidade() == null || entity.getEspecialidade().isBlank())
            throw new FieldValidationException("especialidade", "A especialidade do mecânico é de preenchimento obrigatório.");
        if (entity.getEmail() != null && !entity.getEmail().isBlank())
            if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                throw new FieldValidationException("email", "O e-mail informado não é válido.");
    }

    @Override
    public void validateInsert(MecanicoModel entity) {
        if (repository.existsByCpf(entity.getCpf()))
            throw new RuleValidationException("CPF Duplicado", "Já existe um mecânico cadastrado com esse CPF.");
    }
}