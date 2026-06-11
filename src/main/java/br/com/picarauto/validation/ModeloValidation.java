package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IModeloRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ModeloValidation extends GenericValidation<ModeloModel, IModeloRepository>
        implements IModeloValidation {

    public ModeloValidation(IModeloRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(ModeloModel entity) {
        super.validateFields(entity);

        if (entity.getNomeModelo() == null || entity.getNomeModelo().isBlank())
            throw new FieldValidationException("nomeModelo", "O nome do modelo é de preenchimento obrigatório.");
        entity.setNomeModelo(capitalizar(entity.getNomeModelo().trim()));

        if (entity.getAnoModelo() == null)
            throw new FieldValidationException("anoModelo", "O ano do modelo é de preenchimento obrigatório.");
        if (entity.getAnoModelo() < 1900)
            throw new FieldValidationException("anoModelo", "O ano do modelo não pode ser anterior a 1900.");
        if (entity.getAnoModelo() > LocalDate.now().getYear())
            throw new FieldValidationException("anoModelo", "O ano do modelo não pode ser no futuro.");

        if (entity.getIdMarca() == null)
            throw new FieldValidationException("idMarca", "A marca do modelo é de preenchimento obrigatório.");
    }

    @Override
    public void validateInsert(ModeloModel entity) {
        if (repository.existsByNomeModelo(entity.getNomeModelo()))
            throw new RuleValidationException("Nome Duplicado",
                    "Já existe um modelo cadastrado com esse nome.");
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