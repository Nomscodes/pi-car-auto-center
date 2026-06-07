package br.com.picarauto.factory;

import br.com.picarauto.model.IItemServicoOS;
import br.com.picarauto.model.ItemServicoInternoModel;

/**
 * Fábrica concreta que cria itens de serviço interno para uma OS.
 *
 * Padrão de Projeto: Factory Method
 * Implementação de {@link IServicoItemFactory} responsável por instanciar
 * {@link ItemServicoInternoModel} — serviços executados pelos próprios
 * colaboradores da oficina.
 *
 * @author Caio4breu
 */
public class ServicoInternoFactory implements IServicoItemFactory {

    @Override
    public IItemServicoOS criar() {
        return new ItemServicoInternoModel();
    }
}