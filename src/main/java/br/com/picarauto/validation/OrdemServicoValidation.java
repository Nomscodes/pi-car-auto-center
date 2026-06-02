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
        if (entity.getDescricaoProblema() == null || entity.getDescricaoProblema().isBlank())
            throw new FieldValidationException("descricaoProblema", "A descrição do problema é obrigatória.");
        if (entity.getDescricaoProblema().length() < 5)
            throw new FieldValidationException("descricaoProblema", "A descrição deve ter no mínimo 5 caracteres.");
        if (entity.getCliente() == null || entity.getCliente().getId() == null)
            throw new FieldValidationException("idCliente", "O cliente da ordem de serviço é obrigatório.");
        if (entity.getVeiculo() == null || entity.getVeiculo().getId() == null)
            throw new FieldValidationException("idVeiculo", "O veículo da ordem de serviço é obrigatório.");
        if (entity.getServicosExecutados() == null || entity.getServicosExecutados().isEmpty())
            throw new FieldValidationException("servicosExecutados", "A ordem de serviço deve ter ao menos um serviço.");
        if (entity.getStatusOrdemServico() == OrdemServicoModel.StatusOrdemServico.FINALIZADO
                && entity.getDataConclusao() == null)
            throw new FieldValidationException("dataConclusao", "A data de conclusão é obrigatória para OS finalizada.");
    }
}