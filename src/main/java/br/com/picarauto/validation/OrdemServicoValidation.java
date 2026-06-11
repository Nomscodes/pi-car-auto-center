package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IOrdemServicoRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

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
        // corrigido — findByIdAndAtivoTrue retorna Optional
        Optional<OrdemServicoModel> optAtual = repository.findByIdAndAtivoTrue(entity.getId());
        if (optAtual.isEmpty()) return;
        OrdemServicoModel atual = optAtual.get();
        StatusOrdemServico de = atual.getStatus();
        StatusOrdemServico para = entity.getStatus();
        if (!transicaoValida(de, para))
            throw new RuleValidationException("Transição inválida",
                    "Não é permitido alterar o status de " + de + " para " + para
                    + ". Fluxo esperado: ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.");
    }

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