package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;

/**
 * Ordena as OS pelo valor total calculado, do menor para o maior.
 *
 * Padrão de Projeto: Template Method
 * Subclasse de {@link OrdenadorOS} — implementa apenas o critério de
 * comparação. O algoritmo de ordenação em si está na classe pai.
 *
 * @author Caio4breu
 */
public class OrdenadorPorValor extends OrdenadorOS {

    /**
     * Compara duas OS pelo valor total retornado por {@code calcularTotal()}.
     * OS com valor total nulo são tratadas como zero na comparação.
     */
    @Override
    protected int comparar(OrdemServicoModel a, OrdemServicoModel b) {
        return Double.compare(a.getValorTotal(), b.getValorTotal());
    }
}