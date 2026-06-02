package br.com.picarauto.validation;

import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IVeiculoRepository;

public class VeiculoValidation extends GenericValidation<VeiculoModel, IVeiculoRepository>
        implements IVeiculoValidation {

    public VeiculoValidation(IVeiculoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(VeiculoModel entity) {
        super.validateFields(entity);
        if (entity.getPlaca() == null || entity.getPlaca().isBlank())
            throw new FieldValidationException("placa", "A placa do veículo é de preenchimento obrigatório.");
        String placaNormalizada = entity.getPlaca().replace("-", "").toUpperCase().trim();
        boolean placaAntiga    = placaNormalizada.matches("[A-Z]{3}[0-9]{4}");
        boolean placaMercosul  = placaNormalizada.matches("[A-Z]{3}[0-9][A-Z][0-9]{2}");
        if (!placaAntiga && !placaMercosul)
            throw new FieldValidationException("placa", "Formato de placa inválido. Use o padrão antigo (ABC1234) ou Mercosul (ABC1D23).");
        entity.setPlaca(placaNormalizada);
        if (entity.getMarca() == null || entity.getMarca().isBlank())
            throw new FieldValidationException("marca", "A marca do veículo é de preenchimento obrigatório.");
        if (entity.getModelo() == null || entity.getModelo().isBlank())
            throw new FieldValidationException("modelo", "O modelo do veículo é de preenchimento obrigatório.");
        if (entity.getAnoFabricacao() == null)
            throw new FieldValidationException("anoFabricacao", "O ano de fabricação é de preenchimento obrigatório.");
        if (entity.getCliente() == null || entity.getCliente().getId() == null)
            throw new FieldValidationException("clienteId", "O cliente do veículo é de preenchimento obrigatório.");
    }

    @Override
    public void validateInsert(VeiculoModel entity) {
        if (repository.existsByPlaca(entity.getPlaca()))
            throw new RuleValidationException("Placa Duplicada", "Já existe um veículo com essa placa cadastrado.");
    }
}