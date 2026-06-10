/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.validation;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.repository.IColaboradorRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gabriel
 */
@Component
public class ColaboradorValidation extends GenericValidation<ColaboradorModel, IColaboradorRepository> implements IColaboradorValidation {

    public ColaboradorValidation(IColaboradorRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ColaboradorModel entity) {
        super.validateFields(entity);

        if (entity.getNomeCompleto() == null || entity.getNomeCompleto().isBlank()) {
            throw new FieldValidationException("nomeCompleto",
                    "O nome completo do colaborador é de preenchimento obrigatório.");
        }

        if (entity.getTelefone() == null || entity.getTelefone().isBlank()) {
            throw new FieldValidationException("telefone",
                    "O telefone do colaborador é de preenchimento obrigatório.");
        }

        if (entity.getEmail() == null || entity.getEmail().isBlank()) {
            throw new FieldValidationException("email",
                    "O e-mail do colaborador é de preenchimento obrigatório.");
        }
        if (!entity.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new FieldValidationException("email",
                    "O e-mail informado não é válido.");
        }

        if (entity.getEndereco() == null || entity.getEndereco().isBlank()) {
            throw new FieldValidationException("endereco",
                    "O endereço do colaborador é de preenchimento obrigatório.");
        }

        if (entity.getDataAdmissao() == null) {
            throw new FieldValidationException("dataAdmissao",
                    "A data de admissão é de preenchimento obrigatório.");
        }
        if (entity.getDataAdmissao().isAfter(LocalDate.now())) {
            throw new FieldValidationException("dataAdmissao",
                    "A data de admissão não pode ser no futuro.");
        }

        if (entity.getSalario() <= 0) {
            throw new FieldValidationException("salario",
                    "O salário deve ser maior que zero.");
        }

        if (entity.getFuncao() == null) {
            throw new FieldValidationException("funcao",
                    "A função do colaborador é de preenchimento obrigatório.");
        }
    }
}
