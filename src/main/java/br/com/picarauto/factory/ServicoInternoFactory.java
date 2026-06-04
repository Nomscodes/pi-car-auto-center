package br.com.picarauto.factory;

import br.com.picarauto.model.IItemServicoOS;
import br.com.picarauto.model.OrdemServicoServicoModel;

/**
 * Fábrica concreta que cria itens de serviço interno para uma OS.
 *
 * Padrão de Projeto: Factory Method
 * Implementação de {@link ServicoItemFactory} responsável por instanciar
 * {@link OrdemServicoServicoModel} — serviços executados pelos próprios
 * colaboradores da oficina.
 *
 * @author Caio4breu
 */
public class ServicoInternoFactory implements IServicoItemFactory {

    @Override
    public IItemServicoOS criar() {
        // Cria um item de serviço interno com valores padrão.
        // O controller é responsável por preencher os dados após a criação.
        return new OrdemServicoServicoModel();
    }
}