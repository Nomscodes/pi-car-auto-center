package br.com.picarauto.decorator;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoServicoModel;

/**
 * Decorator concreto: acrescenta a seção de serviços internos ao resumo da OS.
 *
 * Padrão de Projeto: Decorator (decorator concreto)
 *
 * @author Caio4breu
 */
public class ResumoComServicosInternos extends ResumoOSDecorator {

    private final OrdemServicoModel os;

    public ResumoComServicosInternos(IResumoOS decorado, OrdemServicoModel os) {
        super(decorado);
        this.os = os;
    }

    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder(decorado.gerar());
        sb.append("\n--- Serviços Internos ---\n");

        for (OrdemServicoServicoModel item : os.getServicosExecutados()) {
            sb.append("  • ").append(item.getDescricao());
            if (item.getValorCobrado() != null) {
                sb.append("  R$ ").append(item.getValorCobrado());
            }
            if (item.getMecanicoExecutor() != null) {
                sb.append("  (").append(item.getMecanicoExecutor().getNome()).append(")");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}