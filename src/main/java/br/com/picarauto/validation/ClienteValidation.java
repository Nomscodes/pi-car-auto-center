package br.com.picarauto.validation;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IClienteRepository;

public class ClienteValidation extends GenericValidation<ClienteModel, IClienteRepository>
        implements IClienteValidation {

    public ClienteValidation(IClienteRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ClienteModel entity) {
        super.validateFields(entity);
        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome", "O nome do cliente é de preenchimento obrigatório.");
        if (entity.getCpf() == null || entity.getCpf().isBlank())
            throw new FieldValidationException("cpf", "O CPF do cliente é de preenchimento obrigatório.");
        if (entity.getCpf().replaceAll("[^0-9]", "").length() != 11)
            throw new FieldValidationException("cpf", "O CPF deve conter 11 dígitos numéricos.");
        String cpfCompleto = entity.getCpf().replaceAll("[^0-9]", "");
        if (cpfCompleto.chars().distinct().count() == 1)
            throw new FieldValidationException("cpf", "CPF inválido.");
        if (!validarAlgoritmoCPF(cpfCompleto))
            throw new FieldValidationException("cpf", "CPF inválido.");
        if (entity.getEmail() != null && !entity.getEmail().isBlank())
            if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                throw new FieldValidationException("email", "O e-mail informado não é válido.");
    }

    @Override
    public void validateInsert(ClienteModel entity) {
        if (repository.existsByCpf(entity.getCpf()))
            throw new RuleValidationException("CPF Duplicado", "Já existe um cliente cadastrado com esse CPF.");
    }

    private boolean validarAlgoritmoCPF(String cpf) {
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int d1 = 11 - (soma % 11);
        if (d1 >= 10) d1 = 0;
        if (d1 != (cpf.charAt(9) - '0')) return false;
        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        int d2 = 11 - (soma % 11);
        if (d2 >= 10) d2 = 0;
        return d2 == (cpf.charAt(10) - '0');
    }
}