/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.validation;
import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IFornecedorRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */
@Component
public class FornecedorValidation extends GenericValidation<FornecedorModel, IFornecedorRepository> implements IFornecedorValidation {

    public FornecedorValidation(IFornecedorRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(FornecedorModel entity) {
        super.validateFields(entity);

        if (entity.getNomeFornecedor() == null || entity.getNomeFornecedor().isBlank())
            throw new FieldValidationException("nomeFornecedor",
                    "O nome do fornecedor é de preenchimento obrigatório.");
        entity.setNomeFornecedor(entity.getNomeFornecedor().trim());

        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone",
                    "O telefone do fornecedor é de preenchimento obrigatório.");

        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email",
                    "O e-mail do fornecedor é de preenchimento obrigatório.");
        if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new FieldValidationException("email", "O e-mail informado não é válido.");

        if (entity.getCnpj() != null && !entity.getCnpj().isBlank()) {
            String cnpjLimpo = entity.getCnpj().replaceAll("\\D", "");
            if (cnpjLimpo.length() != 14)
                throw new FieldValidationException("cnpj",
                        "O CNPJ deve conter exatamente 14 dígitos numéricos.");
            entity.setCnpj(cnpjLimpo);
        } else {
            entity.setCnpj(null);
        }
    }

    @Override
    public void validateInsert(FornecedorModel entity) {
        if (entity.getCnpj() != null && repository.existsByCnpj(entity.getCnpj())) {
            throw new RuleValidationException("CNPJ Duplicado",
                    "Já existe um fornecedor cadastrado com esse CNPJ.");
        }

        if (repository.existsByTelefone(entity.getTelefone())) {
            throw new RuleValidationException("Telefone Duplicado",
                    "Já existe um fornecedor cadastrado com esse telefone.");
        }
    }
}
