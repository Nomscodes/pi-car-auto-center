package br.com.picarauto.validation;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IUsuarioRepository;


public class UsuarioValidation extends GenericValidation<UsuarioModel, IUsuarioRepository>
        implements IUsuarioValidation {

    public UsuarioValidation(IUsuarioRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(UsuarioModel entity) {
        super.validateFields(entity);
        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome", "O nome do usuário é de preenchimento obrigatório.");
        if (entity.getLogin() == null || entity.getLogin().isBlank())
            throw new FieldValidationException("login", "O login é de preenchimento obrigatório.");
        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email", "O e-mail é de preenchimento obrigatório.");
        if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new FieldValidationException("email", "O e-mail informado não é válido.");
        if (entity.getPerfil() == null || entity.getPerfil().isBlank())
            throw new FieldValidationException("perfil", "O perfil do usuário é de preenchimento obrigatório.");
    }

    @Override
    public void validateInsert(UsuarioModel entity) {
        if (repository.existsByLogin(entity.getLogin()))
            throw new RuleValidationException("Login Duplicado", "Já existe um usuário cadastrado com esse login.");
    }
}