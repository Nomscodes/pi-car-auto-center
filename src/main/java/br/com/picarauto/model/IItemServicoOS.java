package br.com.picarauto.model;

/**
 * Interface base para itens de serviço de uma Ordem de Serviço.
 *
 * Padrão de Projeto: Factory Method
 * Define o contrato comum entre itens de serviço interno e externo,
 * permitindo que o factory crie qualquer tipo sem que o controller
 * precise conhecer as classes concretas.
 *
 * @author Caio4breu
 */
public interface IItemServicoOS {
    Long getId();
    String getDescricao();
}