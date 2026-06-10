package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Índice de lookup por placa do veículo usando Tabela Hash.
 *
 * Estrutura de Dados: Tabela Hash com Encadeamento Exterior
 * Placa é uma chave de texto, única por veículo. O atendente digita a
 * placa exata para consultar. Isso é o caso de uso perfeito para hash:
 * chave exata conhecida → acesso em O(1), sem precisar percorrer nada.
 *
 * Por que encadeamento exterior?
 * Colisões são inevitáveis quando o número de OS cresce. No encadeamento
 * exterior cada posição do vetor é a cabeça de uma lista encadeada.
 * Colisões apenas adicionam um nó na lista daquela posição — a tabela
 * nunca fica "cheia" e não precisa ser redimensionada.
 *
 * A função hash transforma a placa (String) em um índice válido do vetor
 * usando o hashCode nativo do Java com módulo — mesmo princípio de
 * h(x) = x % tamanho ensinado em aula, adaptado para Strings.
 *
 * Responsabilidade separada da FilaOS:
 * a fila gerencia a ORDEM DE ATENDIMENTO.
 * a tabela hash é um ÍNDICE DE CONSULTA POR PLACA — não substitui a fila.
 *
 * @author Caio4breu
 */
public class TabelaHashOS {

    // Nó do encadeamento exterior — cada nó guarda uma OS e aponta para o próximo
    private static class No {
        OrdemServicoModel os;   // OS armazenada nesse nó
        No proximo;             // próximo nó na mesma posição (colisão)

        No(OrdemServicoModel os) {
            this.os = os;
            this.proximo = null;
        }
    }

    private static final int TAMANHO_PADRAO = 64; // tamanho fixo da tabela

    private final No[] tabela;  // vetor de nós: cada posição é cabeça de uma lista encadeada
    private final int tamanho;

    public TabelaHashOS() {
        this(TAMANHO_PADRAO);
    }

    public TabelaHashOS(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new No[tamanho]; // todas as posições começam null (lista vazia)
    }

    // ── Função Hash ───────────────────────────────────────────────────────────

    // Transforma a placa em um índice válido do vetor.
    // Math.abs garante que o resultado seja positivo.
    // % tamanho garante que o resultado fique entre 0 e tamanho-1.
    // Mesmo princípio de h(x) = x % tamanho da aula, adaptado para String.
    private int hash(String placa) {
        return Math.abs(placa.toUpperCase().hashCode()) % tamanho;
    }

    // ── Inserir ───────────────────────────────────────────────────────────────

    /**
     * Insere ou atualiza a OS na tabela usando a placa como chave.
     * OS com placaVeiculo nula são ignoradas — não podem ser indexadas.
     */
    public void inserir(OrdemServicoModel os) {
        if (os == null || os.getPlacaVeiculo() == null) return;

        String placa = os.getPlacaVeiculo().toUpperCase().trim();
        int posicao = hash(placa);
        No novo = new No(os);

        if (tabela[posicao] == null) {
            tabela[posicao] = novo;     // posição vazia — novo nó vira a cabeça da lista
        } else {
            No atual = tabela[posicao];

            // percorre a lista procurando se a placa já está indexada
            while (atual != null) {
                if (placasIguais(atual.os.getPlacaVeiculo(), placa)) {
                    atual.os = os;      // placa já existe — atualiza a OS
                    return;
                }
                if (atual.proximo == null) break; // chegou no último nó
                atual = atual.proximo;
            }

            atual.proximo = novo;       // encadeia o novo nó no final da lista (colisão)
        }
    }

    // ── Buscar ────────────────────────────────────────────────────────────────

    /**
     * Busca a OS pela placa exata em O(1) no caso médio.
     * Retorna null se não encontrar.
     */
    public OrdemServicoModel buscar(String placa) {
        if (placa == null || placa.isBlank()) return null;

        String placaNormalizada = placa.toUpperCase().trim();
        int posicao = hash(placaNormalizada);
        No atual = tabela[posicao]; // vai direto para a posição calculada

        // percorre a lista daquela posição (em caso de colisão pode ter mais de um nó)
        while (atual != null) {
            if (placasIguais(atual.os.getPlacaVeiculo(), placaNormalizada)) {
                return atual.os;        // encontrou — retorna a OS
            }
            atual = atual.proximo;      // avança para o próximo nó da lista
        }

        return null; // percorreu a lista daquela posição e não achou
    }

    /**
     * Busca todas as OS associadas a uma placa.
     * Em condições normais retorna lista com um elemento — uma placa tem um veículo.
     * Retorna lista vazia se não encontrar.
     */
    public List<OrdemServicoModel> buscarTodas(String placa) {
        List<OrdemServicoModel> resultado = new ArrayList<>();
        if (placa == null || placa.isBlank()) return resultado;

        String placaNormalizada = placa.toUpperCase().trim();
        int posicao = hash(placaNormalizada);
        No atual = tabela[posicao];

        while (atual != null) {
            if (placasIguais(atual.os.getPlacaVeiculo(), placaNormalizada)) {
                resultado.add(atual.os);
            }
            atual = atual.proximo;
        }

        return resultado;
    }

    // ── Remover ───────────────────────────────────────────────────────────────

    /**
     * Remove a OS com a placa informada da tabela.
     */
    public void remover(String placa) {
        if (placa == null || placa.isBlank()) return;

        String placaNormalizada = placa.toUpperCase().trim();
        int posicao = hash(placaNormalizada);
        No atual = tabela[posicao];
        No anterior = null;

        while (atual != null) {
            if (placasIguais(atual.os.getPlacaVeiculo(), placaNormalizada)) {

                if (anterior == null) {
                    tabela[posicao] = atual.proximo; // a cabeça da lista passa a ser o próximo nó
                } else {
                    anterior.proximo = atual.proximo; // o anterior pula o nó removido
                }

                return; // removeu — para
            }

            anterior = atual;           // o atual vira o anterior antes de avançar
            atual = atual.proximo;      // avança para o próximo nó
        }
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    /** Retorna true se a placa informada já está indexada na tabela. */
    public boolean contem(String placa) {
        return buscar(placa) != null;
    }

    // Compara duas placas ignorando maiúsculas/minúsculas e espaços
    private boolean placasIguais(String a, String b) {
        if (a == null || b == null) return false;
        return a.toUpperCase().trim().equals(b.toUpperCase().trim());
    }
}