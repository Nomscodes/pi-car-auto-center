package br.com.picarauto.decorator;

import br.com.picarauto.model.ItemPedidoPecaModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IItemPedidoPecaRepository;
import java.util.List;

/**
 * Decorator concreto: acrescenta a seção de peças aplicadas ao resumo da OS.
 *
 * Padrão de Projeto: Decorator (decorator concreto)
 *
 * Recebe o repositório por injeção no construtor e chama findAllByIdOS()
 * para listar os itens reais vinculados à OS, sem acoplar o decorator
 * ao banco diretamente — mesma estratégia usada em ResumoComServicosInternos.
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
public class ResumoComPecas extends ResumoOSDecorator {

    private final OrdemServicoModel os;
    private final IItemPedidoPecaRepository itemPedidoPecaRepository;

    public ResumoComPecas(IResumoOS decorado,
                          OrdemServicoModel os,
                          IItemPedidoPecaRepository itemPedidoPecaRepository) {
        super(decorado);
        this.os = os;
        this.itemPedidoPecaRepository = itemPedidoPecaRepository;
    }

    // Padrão de Projeto: Decorator — adiciona seção de peças ao texto base
    @Override
    public String gerar() {
        StringBuilder sb = new StringBuilder(decorado.gerar());
        sb.append("\n--- Peças Aplicadas ---\n");

        if (os.getId() == null) {
            sb.append("  (OS sem ID — nenhum item carregado)\n");
            return sb.toString();
        }

        List<ItemPedidoPecaModel> itens =
                itemPedidoPecaRepository.findAllByIdOS(os.getId());

        if (itens == null || itens.isEmpty()) {
            sb.append("  Nenhuma peça registrada.\n");
        } else {
            for (ItemPedidoPecaModel item : itens) {
                sb.append("  • ");
                sb.append("Cód. Nacional: ").append(item.getCodigoNacional());
                sb.append("  |  Qtd: ").append(item.getQuantidade());
                if (item.getDataEntrega() != null) {
                    sb.append("  |  Entrega: ").append(item.getDataEntrega());
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}