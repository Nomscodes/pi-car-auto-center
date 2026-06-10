package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que define o algoritmo de ordenação de OS.
 *
 * Padrão de Projeto: Template Method
 * O método {@link #ordenar} implementa o Insertion Sort manualmente e é fixo
 * para todas as subclasses. O critério de comparação é definido pelo método
 * abstrato {@link #comparar}, que cada subclasse implementa à sua maneira.
 *
 * @author Caio4breu
 */
public abstract class OrdenadorOS {
    /**
     * Ordena as OS da fila usando Insertion Sort e retorna uma lista ordenada.
     * A fila original não é alterada — o Iterator é usado apenas para leitura.
     *
     * @param fila a FilaOS com as ordens de serviço a ordenar
     * @return lista de OS ordenadas pelo critério da subclasse
     */
    public List<OrdemServicoModel> ordenar(FilaOS fila) {
        // Copia os elementos da fila para uma lista usando o Iterator
        // sem modificar a fila original
        List<OrdemServicoModel> lista = new ArrayList<>();
        for (OrdemServicoModel os : fila) {
            lista.add(os);
        }

        // Insertion Sort manual — sem uso de bibliotecas (Collections.sort, Arrays.sort etc.)
        // Eficiente para listas pequenas e parcialmente ordenadas, como a fila de OS de uma oficina.
        // A cada iteração, pega o elemento atual e o insere na posição correta
        // entre os elementos já ordenados à sua esquerda.
        for (int i = 1; i < lista.size(); i++) {
            OrdemServicoModel atual = lista.get(i); // elemento a ser inserido na posição correta
            int j = i - 1;

            // Empurra os elementos maiores que "atual" uma posição para a direita
            while (j >= 0 && comparar(lista.get(j), atual) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }

            // Insere "atual" na posição correta
            lista.set(j + 1, atual);
        }

        return lista;
    }

    /**
     * Define o critério de comparação entre duas OS.
     * Implementado por cada subclasse com seu próprio critério.
     *
     * @param a primeira OS
     * @param b segunda OS
     * @return valor negativo se a vem antes de b, positivo se b vem antes de a, zero se iguais
     */
    protected abstract int comparar(OrdemServicoModel a, OrdemServicoModel b);

    /**
     * Busca Binária na lista já ordenada por data de abertura.
     *
     * Pré-requisito: a lista deve estar ordenada pela data de abertura —
     * exatamente o que o OrdenadorPorData.ordenar() garante.
     * A busca binária só funciona em estrutura ordenada: ela descarta metade
     * dos elementos a cada comparação, chegando ao resultado em O(log n).
     * Em 500 OS são no máximo 9 comparações em vez de 500.
     *
     * Retorna a primeira OS encontrada com aquela data, ou null se não existir.
     *
     * @param listaOrdenada lista retornada por OrdenadorPorData.ordenar()
     * @param data          data de abertura a buscar
     * @return OS encontrada ou null
     */
    public OrdemServicoModel buscarBinariaPorData(
            List<OrdemServicoModel> listaOrdenada,
            java.time.LocalDate data) {

        if (listaOrdenada == null || listaOrdenada.isEmpty() || data == null) return null;

        int inicio = 0;
        int fim = listaOrdenada.size() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;                  // posição do elemento do meio
            OrdemServicoModel osMeio = listaOrdenada.get(meio);

            if (osMeio.getDataAbertura() == null) {
                fim = meio - 1;                             // OS sem data ficam no fim — descarta a direita
                continue;
            }

            int comparacao = osMeio.getDataAbertura().compareTo(data);

            if (comparacao == 0) {
                return osMeio;                              // achou a data — retorna
            } else if (comparacao < 0) {
                inicio = meio + 1;                          // data do meio é anterior — descarta a esquerda
            } else {
                fim = meio - 1;                             // data do meio é posterior — descarta a direita
            }
        }

        return null; // data não encontrada na lista
    }
}