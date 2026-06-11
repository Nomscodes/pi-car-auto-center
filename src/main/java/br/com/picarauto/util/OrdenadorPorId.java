package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;

/**
 * Ordena as OS pelo ID, do menor para o maior (ASC) ou do maior para o menor (DESC).
 *
 * Padrão de Projeto: Template Method
 * Subclasse de {@link OrdenadorOS} — implementa apenas o critério de
 * comparação. O algoritmo de ordenação em si está na classe pai.
 *
 * Use {@code ordenar(fila)} para crescente
 * ou {@code ordenar(fila, Direcao.DESC)} para decrescente.
 *
 * @author Caio4breu
 */
public class OrdenadorPorId extends OrdenadorOS {

    /**
     * Compara duas OS pelo ID.
     * OS com ID nulo são enviadas para o fim da lista.
     */
    @Override
    protected int comparar(OrdemServicoModel a, OrdemServicoModel b) {
        if (a.getId() == null && b.getId() == null) return 0;
        if (a.getId() == null) return 1;
        if (b.getId() == null) return -1;
        return Long.compare(a.getId(), b.getId());
    }
}