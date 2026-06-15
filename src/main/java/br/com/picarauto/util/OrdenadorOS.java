package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * Direção da ordenação.
     * ASC = crescente (padrão), DESC = decrescente.
     */
    public enum Direcao {
        ASC, DESC
    }

    /**
     * Ordena as OS da fila usando Insertion Sort em ordem crescente (ASC).
     * Atalho para {@link #ordenar(FilaOS, Direcao)} com {@code Direcao.ASC}.
     *
     * @param fila a FilaOS com as ordens de serviço a ordenar
     * @return lista de OS ordenadas de forma crescente pelo critério da subclasse
     */
    public List<OrdemServicoModel> ordenar(FilaOS fila) {
        return ordenar(fila, Direcao.ASC);
    }

    /**
     * Ordena as OS da fila usando Insertion Sort e retorna uma lista ordenada.
     * A fila original não é alterada — o Iterator é usado apenas para leitura.
     *
     * @param fila    a FilaOS com as ordens de serviço a ordenar
     * @param direcao {@code ASC} para crescente, {@code DESC} para decrescente
     * @return lista de OS ordenadas pelo critério da subclasse na direção escolhida
     */
    public List<OrdemServicoModel> ordenar(FilaOS fila, Direcao direcao) {
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

            // Para DESC, invertemos o sinal da comparação — o algoritmo em si não muda,
            // apenas o critério de "quem vem antes" é espelhado.
            while (j >= 0 && comparacaoComDirecao(lista.get(j), atual, direcao) > 0) {
                lista.set(j + 1, lista.get(j));
                j--;
            }

            // Insere "atual" na posição correta
            lista.set(j + 1, atual);
        }

        return lista;
    }

    /**
     * Aplica a direção sobre o resultado bruto de {@link #comparar}.
     * ASC não altera nada; DESC inverte o sinal.
     */
    private int comparacaoComDirecao(OrdemServicoModel a, OrdemServicoModel b, Direcao direcao) {
        int resultado = comparar(a, b);
        return direcao == Direcao.DESC ? -resultado : resultado;
    }

    /**
     * Agrupa as OS da fila por status, preservando a ordem de inserção dos grupos.
     * Dentro de cada grupo as OS ficam na ordem de chegada na fila (FIFO).
     * Não altera a fila original.
     *
     * A ordem dos grupos no mapa segue a declaração do enum StatusOrdemServico:
     * ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.
     *
     * @param fila a FilaOS com as ordens de serviço a agrupar
     * @return mapa de status → lista de OS, com grupos na ordem do enum
     */
    public Map<OrdemServicoModel.StatusOrdemServico, List<OrdemServicoModel>> agruparPorStatus(FilaOS fila) {
        // LinkedHashMap preserva a ordem de inserção das chaves.
        // Populamos com todos os status do enum primeiro para garantir
        // que a ordem sempre seja ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO,
        // mesmo que algum grupo esteja vazio.
        Map<OrdemServicoModel.StatusOrdemServico, List<OrdemServicoModel>> grupos = new LinkedHashMap<>();
        for (OrdemServicoModel.StatusOrdemServico status : OrdemServicoModel.StatusOrdemServico.values()) {
            grupos.put(status, new ArrayList<>());
        }

        for (OrdemServicoModel os : fila) {
            if (os.getStatus() != null) {
                grupos.get(os.getStatus()).add(os);
            }
        }

        return grupos;
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

    /**
     * Busca TODAS as OS de uma determinada data na lista já ordenada por data.
     *
     * Usa a busca binária para encontrar qualquer OS com aquela data em O(log n),
     * depois expande linearmente para os vizinhos — coleta todas as OS contíguas
     * com a mesma data sem percorrer a lista inteira.
     *
     * @param listaOrdenada lista retornada por OrdenadorPorData.ordenar()
     * @param data          data de abertura a buscar
     * @return lista (possivelmente vazia) com todas as OS daquela data
     */
    public List<OrdemServicoModel> buscarTodasPorData(
            List<OrdemServicoModel> listaOrdenada,
            java.time.LocalDate data) {

        List<OrdemServicoModel> resultado = new ArrayList<>();
        if (listaOrdenada == null || listaOrdenada.isEmpty() || data == null) return resultado;

        // 1. Busca binária para encontrar qualquer índice com a data
        int inicio = 0;
        int fim = listaOrdenada.size() - 1;
        int indiceEncontrado = -1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            OrdemServicoModel osMeio = listaOrdenada.get(meio);

            if (osMeio.getDataAbertura() == null) {
                fim = meio - 1;
                continue;
            }

            int comparacao = osMeio.getDataAbertura().compareTo(data);
            if (comparacao == 0) {
                indiceEncontrado = meio;
                break;
            } else if (comparacao < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        if (indiceEncontrado == -1) return resultado; // data não existe

        // 2. Expande para a esquerda coletando todos com a mesma data
        int esq = indiceEncontrado;
        while (esq > 0 && data.equals(listaOrdenada.get(esq - 1).getDataAbertura())) {
            esq--;
        }

        // 3. Expande para a direita coletando todos com a mesma data
        int dir = indiceEncontrado;
        while (dir < listaOrdenada.size() - 1 && data.equals(listaOrdenada.get(dir + 1).getDataAbertura())) {
            dir++;
        }

        // 4. Coleta o intervalo [esq, dir]
        for (int i = esq; i <= dir; i++) {
            resultado.add(listaOrdenada.get(i));
        }

        return resultado;
    }
}