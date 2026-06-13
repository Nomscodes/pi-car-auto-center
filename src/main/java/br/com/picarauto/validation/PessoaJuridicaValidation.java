package br.com.picarauto.validation;

import br.com.picarauto.model.PessoaJuridicaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IPessoaJuridicaRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class PessoaJuridicaValidation extends GenericValidation<PessoaJuridicaModel, IPessoaJuridicaRepository>
        implements IPessoaJuridicaValidation {

    public PessoaJuridicaValidation(IPessoaJuridicaRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(PessoaJuridicaModel entity) {
        super.validateFields(entity);

        // ── Razão Social ──────────────────────────────────────────────────────
        if (entity.getRazaoSocial() == null || entity.getRazaoSocial().isBlank())
            throw new FieldValidationException("razaoSocial",
                    "A razão social é de preenchimento obrigatório.");
        entity.setRazaoSocial(capitalizarNome(entity.getRazaoSocial()));
        // nomeCompleto espelha a razão social em PJ
        entity.setNomeCompleto(entity.getRazaoSocial());

        // ── Nome Fantasia (opcional) ───────────────────────────────────────────
        if (entity.getNomeFantasia() != null && !entity.getNomeFantasia().isBlank())
            entity.setNomeFantasia(capitalizarNome(entity.getNomeFantasia()));

        // ── CNPJ ──────────────────────────────────────────────────────────────
        // O banco armazena apenas os 14 dígitos (length = 14).
        // A view já envia sem formatação; aqui garantimos a limpeza e o comprimento.
        if (entity.getCnpj() == null || entity.getCnpj().isBlank())
            throw new FieldValidationException("cnpj",
                    "O CNPJ é de preenchimento obrigatório.");
        String cnpjLimpo = entity.getCnpj().replaceAll("\\D", "");
        if (cnpjLimpo.length() != 14)
            throw new FieldValidationException("cnpj",
                    "O CNPJ deve conter exatamente 14 dígitos numéricos.");
        entity.setCnpj(cnpjLimpo);

        // ── Data de abertura ──────────────────────────────────────────────────
        if (entity.getDataAbertura() == null)
            throw new FieldValidationException("dataAbertura",
                    "A data de abertura é obrigatória.");

        // ── Telefone ──────────────────────────────────────────────────────────
        // Armazenado formatado (length = 20 em PessoaModel).
        // Aceita celular (11 dígitos) e fixo (10 dígitos), ambos com DDD.
        if (entity.getTelefone() == null || entity.getTelefone().isBlank())
            throw new FieldValidationException("telefone",
                    "O telefone é de preenchimento obrigatório.");
        entity.setTelefone(formatarTelefone(entity.getTelefone()));

        // ── E-mail ────────────────────────────────────────────────────────────
        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new FieldValidationException("email",
                    "O e-mail é de preenchimento obrigatório.");
        if (!entity.getEmail().matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$"))
            throw new FieldValidationException("email",
                    "O e-mail informado não é válido.");

        // ── Endereço ──────────────────────────────────────────────────────────
        if (entity.getEndereco() == null || entity.getEndereco().isBlank())
            throw new FieldValidationException("endereco",
                    "O endereço é de preenchimento obrigatório.");

        // ── Data de cadastro ──────────────────────────────────────────────────
        if (entity.getDataCadastro() == null)
            throw new FieldValidationException("dataCadastro",
                    "A data de cadastro é obrigatória.");
    }

    @Override
    public void validateInsert(PessoaJuridicaModel entity) {
        if (repository.existsByCnpj(entity.getCnpj()))
            throw new RuleValidationException("cnpj",
                    "Já existe um cliente cadastrado com esse CNPJ.");
    }

    // ── Helpers de formatação ─────────────────────────────────────────────────

    /**
     * Capitaliza a primeira letra de cada palavra.
     * Exemplo: "empresa ltda" → "Empresa Ltda"
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
     * Formata telefone com DDD.
     * 11 dígitos (celular): (47) 99111-2222
     * 10 dígitos (fixo):    (47) 3300-4444
     * Outros: retorna como recebido.
     */
    private String formatarTelefone(String telefone) {
        String digitos = telefone.replaceAll("\\D", "");
        if (digitos.length() == 11)
            return "(" + digitos.substring(0, 2) + ") "
                    + digitos.substring(2, 7) + "-" + digitos.substring(7);
        if (digitos.length() == 10)
            return "(" + digitos.substring(0, 2) + ") "
                    + digitos.substring(2, 6) + "-" + digitos.substring(6);
        return telefone;
    }
}