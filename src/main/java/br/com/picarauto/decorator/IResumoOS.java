package br.com.picarauto.decorator;

/**
 * Interface do Padrão de Projeto Decorator aplicado à geração de resumo de OS.
 *
 * Padrão de Projeto: Decorator
 * Define o contrato comum entre o componente base e todos os decoradores,
 * permitindo composição dinâmica do resumo conforme o conteúdo da OS.
 *
 * @author Caio4breu
 */
public interface IResumoOS {
    String gerar();
}