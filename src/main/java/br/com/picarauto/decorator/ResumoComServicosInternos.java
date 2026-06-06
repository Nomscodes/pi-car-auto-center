package br.com.picarauto.decorator;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IItemServicoInternoRepository;
import java.util.List;

/**
 * Decorator concreto: acrescenta a seção de serviços internos ao resumo da OS.
 *
 * Padrão de Projeto: Decorator (decorator concreto)
 *
 * Recebe o repositório por injeção no construtor — mesma estratégia usada
 * em {@link ResumoComPecas} — e chama {@code findAllByOrdemServicoId()} para listar
 * os itens reais vinculados à OS, sem acoplar o decorator ao banco diretamente.
 *
 * Exemplo de uso:
 * <pre>
 *   IResumoOS resumo = new ResumoOSBase(os);
 *   resumo = new ResumoComServicosInternos(resumo, os, itemServicoInternoRepository);
 *   resumo = new ResumoComPecas(resumo, os, itemPedidoPecaRepository);
 *   String textoFinal = resumo.gerar();
 * </pre>
 *
 * @author Caio4breu
 */
public class ResumoComServicosInternos extends ResumoOSDecorator {

    private final OrdemServicoModel os;
    private final IItemServicoInternoRepository itemServicoInternoRepository;

    public ResumoComServicosInternos(IResumoOS decorado,
                                     OrdemServicoModel os,
                                     IItemServicoInternoRepository itemServicoInternoRepository) {
        super(decorado);
        this.os = os;
        this.itemServicoInternoRepository = itemServicoInternoRepository;
    }

    // Padrão de Projeto: Decorator — adiciona seção de serviços internos ao texto base
    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder(decorado.gerar());
        sb.append("\n--- Serviços Internos ---\n");
        if (os.getId() == null) {
            sb.append("  (OS sem ID — nenhum item carregado)\n");
            return sb.toString();
        }
        List<ItemServicoInternoModel> itens =
                itemServicoInternoRepository.findAllByOrdemServicoId(os.getId());
        if (itens == null || itens.isEmpty()) {
            sb.append("  Nenhum serviço interno registrado.\n");
        } else {
            for (ItemServicoInternoModel item : itens) {
                sb.append("  • ");
                sb.append(item.getObservacoes() != null ? item.getObservacoes() : "Sem descrição");
                sb.append("  |  Valor: R$ ").append(String.format("%.2f", item.getValorItem()));
                sb.append("  |  Garantia: ").append(item.getGarantia()).append(" dia(s)");
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}