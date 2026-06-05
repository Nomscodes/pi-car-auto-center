package br.com.picarauto.factory;

import br.com.picarauto.model.IItemServicoOS;
import br.com.picarauto.model.ItemPedidoServicoExternoModel;

/**
 * Fábrica concreta que cria itens de serviço externo para uma OS.
 *
 * Padrão de Projeto: Factory Method
 * Implementação de {@link ServicoItemFactory} responsável por instanciar
 * {@link ItemPedidoServicoExternoModel} — serviços terceirizados para
 * fornecedores parceiros da oficina.
 *
 * @author Caio4breu
 */
public class ServicoExternoFactory implements IServicoItemFactory {

    @Override
    public IItemServicoOS criar() {
        // Cria um item de serviço externo com valores padrão.
        // O controller é responsável por preencher os dados após a criação.
        return new ItemPedidoServicoExternoModel();
    }
}