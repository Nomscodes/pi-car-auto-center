package br.com.picarauto.validation;

import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IPessoaFisicaRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
public class PessoaFisicaValidation extends GenericValidation<PessoaFisicaModel, IPessoaFisicaRepository>
        implements IPessoaFisicaValidation {

    public PessoaFisicaValidation(IPessoaFisicaRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(PessoaFisicaModel entity) {
        super.validateFields(entity);

        // ── Nome completo ─────────────────────────────────────────────────────
        if (entity.getNomeCompleto() == null || entity.getNomeCompleto().isBlank())
            throw new FieldValidationException("nomeCompleto",
                    "O nome completo é de preenchimento obrigatório.");
        entity.setNomeCompleto(capitalizarNome(entity.getNomeCompleto()));

        // ── CPF ───────────────────────────────────────────────────────────────
        // O banco armazena apenas os 11 dígitos (length = 11).
        // A view já envia sem formatação; aqui garantimos a limpeza e o comprimento.
        if (entity.getCpf() == null || entity.getCpf().isBlank())
            throw new FieldValidationException("cpf",
                    "O CPF é de preenchimento obrigatório.");
        String cpfLimpo = entity.getCpf().replaceAll("\\D", "");
        if (cpfLimpo.length() != 11)
            throw new FieldValidationException("cpf",
                    "O CPF deve conter exatamente 11 dígitos numéricos.");
        entity.setCpf(cpfLimpo);

        // ── RG ────────────────────────────────────────────────────────────────
        if (entity.getRg() == null || entity.getRg().isBlank())
            throw new FieldValidationException("rg",
                    "O RG é de preenchimento obrigatório.");
        entity.setRg(entity.getRg().trim());

        // ── Data de nascimento ────────────────────────────────────────────────
        if (entity.getDataNascimento() == null)
            throw new FieldValidationException("dataNascimento",
                    "A data de nascimento é obrigatória.");

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
    public void validateInsert(PessoaFisicaModel entity) {
        if (repository.existsByCpf(entity.getCpf()))
            throw new RuleValidationException("cpf",
                    "Já existe um cliente cadastrado com esse CPF.");
    }

    // ── Helpers de formatação ─────────────────────────────────────────────────

    /**
     * Capitaliza a primeira letra de cada palavra do nome.
     * Exemplo: "joao da silva" → "Joao Da Silva"
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