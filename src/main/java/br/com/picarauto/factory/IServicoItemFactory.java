package br.com.picarauto.factory;

import br.com.picarauto.model.IItemServicoOS;

/**
 * Interface da fábrica de itens de serviço de uma OS.
 *
 * Padrão de Projeto: Factory Method
 * Cada implementação concreta é responsável por criar um tipo específico
 * de item de serviço. O controller depende apenas desta interface,
 * sem conhecer as classes concretas que serão instanciadas.
 *
 * @author Caio4breu
 */
public interface IServicoItemFactory {

    /**
     * Cria e retorna um item de serviço pronto para ser vinculado a uma OS.
     *
     * @return um {@link ItemServicoOS} do tipo correspondente à implementação
     */
    IItemServicoOS criar();
}