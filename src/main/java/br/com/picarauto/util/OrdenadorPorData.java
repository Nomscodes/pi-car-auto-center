package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;

/**
 * Ordena as OS pela data de abertura, da mais antiga para a mais recente.
 *
 * Padrão de Projeto: Template Method
 * Subclasse de {@link OrdenadorOS} — implementa apenas o critério de
 * comparação. O algoritmo de ordenação em si está na classe pai.
 *
 * @author Caio4breu
 */
public class OrdenadorPorData extends OrdenadorOS {

    /**
     * Compara duas OS pela data de abertura.
     * OS com dataAbertura nula são enviadas para o fim da lista.
     */
    @Override
    protected int comparar(OrdemServicoModel a, OrdemServicoModel b) {
        if (a.getDataAbertura() == null && b.getDataAbertura() == null) return 0;
        if (a.getDataAbertura() == null) return 1;
        if (b.getDataAbertura() == null) return -1;
        return a.getDataAbertura().compareTo(b.getDataAbertura());
    }
}