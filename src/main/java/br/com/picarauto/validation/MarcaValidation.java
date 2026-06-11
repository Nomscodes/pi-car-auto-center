package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IMarcaRepository;
import org.springframework.stereotype.Component;

@Component
public class MarcaValidation extends GenericValidation<MarcaModel, IMarcaRepository>
        implements IMarcaValidation {

    public MarcaValidation(IMarcaRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(MarcaModel entity) {
        super.validateFields(entity);
        if (entity.getNome() == null || entity.getNome().isBlank())
            throw new FieldValidationException("nome", "O nome da marca é de preenchimento obrigatório.");
        entity.setNome(capitalizar(entity.getNome().trim()));
    }

    @Override
    public void validateInsert(MarcaModel entity) {
        if (repository.existsByNome(entity.getNome()))
            throw new RuleValidationException("Nome Duplicado",
                    "Já existe uma marca cadastrada com esse nome.");
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) return texto;
        String[] palavras = texto.toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                resultado.append(Character.toUpperCase(palavra.charAt(0)))
                         .append(palavra.substring(1))
                         .append(" ");
            }
        }
        return resultado.toString().trim();
    }
}