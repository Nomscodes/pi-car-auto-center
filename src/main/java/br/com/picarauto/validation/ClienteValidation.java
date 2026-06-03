package br.com.picarauto.validation;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.repository.IClienteRepository;

public class ClienteValidation extends GenericValidation<ClienteModel, IClienteRepository>
        implements IClienteValidation {

    public ClienteValidation(IClienteRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ClienteModel entity) {
        super.validateFields(entity);

        if (entity.getNomeCompleto() == null || entity.getNomeCompleto().isBlank())
            throw new FieldValidationException("nomeCompleto", "O nome do cliente é de preenchimento obrigatório.");

        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone", "O telefone do cliente é de preenchimento obrigatório.");

        if (entity.getEmail() != null && !entity.getEmail().isBlank())
            if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                throw new FieldValidationException("email", "O e-mail informado não é válido.");

        if (entity.getDataCadastro() == null)
            throw new FieldValidationException("dataCadastro", "A data de cadastro é obrigatória.");
    }

    @Override
    public void validateInsert(ClienteModel entity) {
        // Validações de unicidade específicas ficam em PessoaFisicaValidation e PessoaJuridicaValidation
    }
}