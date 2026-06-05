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

    // ResumoComServicosInternos
    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder(decorado.gerar());
        sb.append("\n--- Serviços Internos ---\n");
        // TODO: implementar via ItemServicoInternoRepository quando a view estiver pronta
        return sb.toString();
    }
}