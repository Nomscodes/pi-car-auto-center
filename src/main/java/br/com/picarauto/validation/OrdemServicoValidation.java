package br.com.picarauto.validation;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IOrdemServicoRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author Caio4breu
 */
@Component
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
        if (entity.getStatus() == StatusOrdemServico.FINALIZADO && entity.getDataFechamento() == null)
            throw new FieldValidationException("dataFechamento",
                    "A data de fechamento é obrigatória para OS finalizada.");
    }

    @Override
    public void validateUpdate(OrdemServicoModel entity) {
        OrdemServicoModel atual = repository.findByIdAndAtivoTrue(entity.getId()).orElse(null);
        if (atual == null) return;
        StatusOrdemServico de = atual.getStatus();
        StatusOrdemServico para = entity.getStatus();
        if (!transicaoValida(de, para))
            throw new RuleValidationException("Transição inválida",
                    "Não é permitido alterar o status de " + de + " para " + para
                    + ". Fluxo esperado: ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.");
    }

    /**
     * Define as transições de status permitidas.
     * Fluxo: orcamento → execucao → pagamento → finalizado
     * Não é permitido voltar nem pular etapas.
     */
    private boolean transicaoValida(StatusOrdemServico de, StatusOrdemServico para) {
        if (de == para) return true;
        return switch (de) {
            case ORCAMENTO  -> para == StatusOrdemServico.EXECUCAO;
            case EXECUCAO   -> para == StatusOrdemServico.PAGAMENTO;
            case PAGAMENTO  -> para == StatusOrdemServico.FINALIZADO;
            case FINALIZADO -> false;
        };
    }
}