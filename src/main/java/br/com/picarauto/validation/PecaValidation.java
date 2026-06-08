package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IPecaRepository;
import org.springframework.stereotype.Component;

@Component
public class PecaValidation extends GenericValidation<PecaModel, IPecaRepository>
        implements IPecaValidation {

    public PecaValidation(IPecaRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(PecaModel entity) {
        super.validateFields(entity);
        if (entity.getCodigoNacional() == null)
            throw new FieldValidationException("codigoNacional", "O código nacional é de preenchimento obrigatório.");
        if (entity.getModelo() == null || entity.getModelo().isBlank())
            throw new FieldValidationException("modelo", "O modelo da peça é de preenchimento obrigatório.");
        if (entity.getMarca() == null || entity.getMarca().isBlank())
            throw new FieldValidationException("marca", "A marca da peça é de preenchimento obrigatório.");
        if (entity.getAnoVeiculo() == null || entity.getAnoVeiculo() < 1900)
            throw new FieldValidationException("anoVeiculo", "O ano do veículo é inválido (mínimo 1900).");
        if (entity.getAnoModelo() == null || entity.getAnoModelo() < 1900)
            throw new FieldValidationException("anoModelo", "O ano do modelo é inválido (mínimo 1900).");
        if (entity.getPrecoUnitario() <= 0)
            throw new FieldValidationException("precoUnitario", "O preço unitário deve ser maior que zero.");
        if (entity.getGarantia() == null || entity.getGarantia() < 0)
            throw new FieldValidationException("garantia", "A garantia não pode ser negativa.");
        if (entity.getIdFornecedor() == null)
            throw new FieldValidationException("idFornecedor", "O fornecedor é de preenchimento obrigatório.");
    }

    @Override
    public void validateInsert(PecaModel entity) {
        if (repository.existsByCodigoNacional(entity.getCodigoNacional()))
            throw new RuleValidationException("Código Duplicado",
                    "Já existe uma peça cadastrada com esse código nacional.");
    }

    @Override
    public void validateUpdate(PecaModel entity) {
        if (!repository.existsByCodigoNacional(entity.getCodigoNacional()))
            throw new RuleValidationException("Peça não encontrada",
                    "Nenhuma peça encontrada com o código nacional informado.");
    }
}