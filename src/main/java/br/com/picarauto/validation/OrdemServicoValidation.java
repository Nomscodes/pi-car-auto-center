package br.com.picarauto.validation;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.repository.IOrdemServicoRepository;

public class OrdemServicoValidation extends GenericValidation<OrdemServicoModel, IOrdemServicoRepository>
        implements IOrdemServicoValidation {

    public OrdemServicoValidation(IOrdemServicoRepository repository) {
        super(repository);
    }

    @Override
    public void validateFields(OrdemServicoModel entity) {
        super.validateFields(entity);
        if (entity.getIdVeiculo() == null)
            throw new FieldValidationException("idVeiculo", "O veículo da ordem de serviço é obrigatório.");
        if (entity.getStatus() == null)
            throw new FieldValidationException("status", "O status da ordem de serviço é obrigatório.");
        if (entity.getStatus() == OrdemServicoModel.StatusOrdemServico.FINALIZADO
                && entity.getDataFechamento() == null)
            throw new FieldValidationException("dataFechamento", "A data de fechamento é obrigatória para OS finalizada.");
    }
}