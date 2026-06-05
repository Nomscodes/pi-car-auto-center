package br.com.picarauto.decorator;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoPecaModel;

/**
 * Decorator concreto: acrescenta a seção de peças aplicadas ao resumo da OS.
 *
 * Padrão de Projeto: Decorator (decorator concreto)
 *
 * @author Caio4breu
 */
public class ResumoComPecas extends ResumoOSDecorator {

    private final OrdemServicoModel os;

    public ResumoComPecas(IResumoOS decorado, OrdemServicoModel os) {
        super(decorado);
        this.os = os;
    }

    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder(decorado.gerar());
        sb.append("\n--- Peças Aplicadas ---\n");

//        for (OrdemServicoPecaModel item : os.getPecasAplicadas()) {
//            sb.append("  • ");
//            if (item.getPeca() != null) {
//                sb.append(item.getPeca().getNome());
//            }
//            sb.append("  Qtd: ").append(item.getQuantidade());
//            sb.append("  Unit: R$ ").append(item.getValorUnitario());
//            sb.append("  Total: R$ ").append(item.getValorTotal());
//            sb.append("\n");
//        }

        return sb.toString();
    }
}