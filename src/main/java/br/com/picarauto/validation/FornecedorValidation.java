package br.com.picarauto.validation;

import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IFornecedorRepository;
import org.springframework.stereotype.Component;

/**
 * Validação e padronização dos dados do fornecedor antes de persistir.
 *
 * Padrões aplicados:
 *   nomeFornecedor — capitaliza a primeira letra de cada palavra
 *   telefone       — formata como (00) 00000-0000 ou (00) 0000-0000
 *   email          — converte para minúsculo
 *   cnpj           — armazenado como 14 dígitos puros (formatação visual é da view)
 *
 * @author Gabriel
 */
@Component
public class FornecedorValidation
        extends GenericValidation<FornecedorModel, IFornecedorRepository>
        implements IFornecedorValidation {

    public FornecedorValidation(IFornecedorRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(FornecedorModel entity) {
        super.validateFields(entity);

        // ── Razão Social ──────────────────────────────────────────────────────
        if (entity.getNomeFornecedor() == null || entity.getNomeFornecedor().isBlank())
            throw new FieldValidationException("nomeFornecedor",
                    "O nome do fornecedor é de preenchimento obrigatório.");
        entity.setNomeFornecedor(capitalizarNome(entity.getNomeFornecedor()));

        // ── Telefone ──────────────────────────────────────────────────────────
        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone",
                    "O telefone do fornecedor é de preenchimento obrigatório.");
        String digsTel = entity.getTelefone().replaceAll("\\D", "");
        if (digsTel.length() < 10 || digsTel.length() > 11)
            throw new FieldValidationException("telefone",
                    "O telefone deve ter 10 ou 11 dígitos (com DDD).");
        entity.setTelefone(formatarTelefone(digsTel));

        // ── E-mail ────────────────────────────────────────────────────────────
        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email",
                    "O e-mail do fornecedor é de preenchimento obrigatório.");
        entity.setEmail(entity.getEmail().trim().toLowerCase());
        if (!entity.getEmail().matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$"))
            throw new FieldValidationException("email",
                    "O e-mail informado não é válido.");

        // ── CNPJ (opcional) ───────────────────────────────────────────────────
        // Banco: VARCHAR(14) — armazenamos apenas os 14 dígitos puros.
        // Formatação visual (00.000.000/0000-00) é responsabilidade da view.
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
        if (entity.getCnpj() != null && repository.existsByCnpj(entity.getCnpj()))
            throw new RuleValidationException("cnpj",
                    "Já existe um fornecedor cadastrado com esse CNPJ.");

        if (repository.existsByTelefone(entity.getTelefone()))
            throw new RuleValidationException("telefone",
                    "Já existe um fornecedor cadastrado com esse telefone.");
    }

    // ── Helpers de padronização ───────────────────────────────────────────────

    /**
     * Capitaliza a primeira letra de cada palavra.
     * Ex.: "distribuidora abc ltda" → "Distribuidora Abc Ltda"
     */
    private String capitalizarNome(String nome) {
        String[] palavras = nome.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                sb.append(Character.toUpperCase(palavra.charAt(0)));
                sb.append(palavra.substring(1));
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * Formata telefone com DDD a partir de dígitos puros.
     * 11 dígitos (celular): (62) 99999-0000
     * 10 dígitos (fixo):    (62) 3333-0000
     */
    private String formatarTelefone(String digitos) {
        if (digitos.length() == 11)
            return "(" + digitos.substring(0, 2) + ") "
                    + digitos.substring(2, 7) + "-" + digitos.substring(7);
        return "(" + digitos.substring(0, 2) + ") "
                + digitos.substring(2, 6) + "-" + digitos.substring(6);
    }
}