package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Índice de busca rápida por id das Ordens de Serviço.
 *
 * Estrutura de Dados: Árvore Binária de Busca (BST)
 * O id da OS é único e numérico — dois requisitos perfeitos para uma BST.
 * A busca sequencial na FilaOS é O(n). Com a árvore, a busca por id
 * cai para O(log n): em 500 OS são no máximo 9 comparações em vez de 500.
 *
 * Responsabilidade separada da FilaOS:
 * a fila gerencia a ORDEM DE ATENDIMENTO.
 * a árvore é um ÍNDICE DE CONSULTA — não substitui a fila.
 *
 * Os percursos (em ordem, pré-ordem, pós-ordem) seguem o mesmo padrão
 * implementado nas aulas de Estrutura de Dados.
 *
 * @author Caio4breu
 */
public class ArvoreOS {

    // Nó interno da árvore — guarda a OS e as referências para os filhos
    private static class No {
        OrdemServicoModel os;   // dado armazenado nesse nó
        No esquerda;            // filho esquerdo: ids menores
        No direita;             // filho direito: ids maiores

        No(OrdemServicoModel os) {
            this.os = os;
            this.esquerda = null;
            this.direita = null;
        }
    }

    private No raiz; // ponto de entrada da árvore (null = árvore vazia)

    public ArvoreOS() {
        this.raiz = null;
    }

    // ── Inserir ───────────────────────────────────────────────────────────────

    /**
     * Insere uma OS na árvore usando seu id como chave de ordenação.
     * OS com id nulo são ignoradas — não podem ser indexadas.
     */
    public void inserir(OrdemServicoModel os) {
        if (os == null || os.getId() == null) return;
        raiz = inserirRecursivo(raiz, os);
    }

    // percorre a árvore até achar a posição correta pelo id
    private No inserirRecursivo(No atual, OrdemServicoModel os) {
        if (atual == null) {
            return new No(os); // posição vazia encontrada — insere aqui
        }

        long idNovo = os.getId();
        long idAtual = atual.os.getId();

        if (idNovo < idAtual) {                                         // id menor: vai para a esquerda
            atual.esquerda = inserirRecursivo(atual.esquerda, os);
        } else if (idNovo > idAtual) {                                  // id maior: vai para a direita
            atual.direita = inserirRecursivo(atual.direita, os);
        }
        // idNovo == idAtual: id duplicado — atualiza a OS no nó existente
        else {
            atual.os = os;
        }

        return atual;
    }

    // ── Buscar ────────────────────────────────────────────────────────────────

    /**
     * Busca uma OS pelo id em O(log n).
     * Retorna null se não encontrar.
     */
    public OrdemServicoModel buscar(Long id) {
        if (id == null) return null;
        return buscarRecursivo(raiz, id);
    }

    // segue o caminho correto baseado na comparação do id — igual ao da aula
    private OrdemServicoModel buscarRecursivo(No atual, Long id) {
        if (atual == null) {        // chegou em nó vazio: id não existe na árvore
            return null;
        }

        long idAtual = atual.os.getId();

        if (id == idAtual) {        // achou o nó com esse id
            return atual.os;
        }

        if (id < idAtual) {         // id buscado é menor: desce pela esquerda
            return buscarRecursivo(atual.esquerda, id);
        } else {                    // id buscado é maior: desce pela direita
            return buscarRecursivo(atual.direita, id);
        }
    }

    // ── Remover ───────────────────────────────────────────────────────────────

    /**
     * Remove a OS com o id informado da árvore.
     * Trata os 3 casos clássicos de remoção em BST.
     */
    public void remover(Long id) {
        if (id == null) return;
        raiz = removerRecursivo(raiz, id);
    }

    // encontra o nó e trata os 3 casos de remoção — mesmo padrão da aula
    private No removerRecursivo(No atual, Long id) {
        if (atual == null) return null; // id não encontrado na árvore

        long idAtual = atual.os.getId();

        if (id < idAtual) {                                             // id menor: desce pela esquerda
            atual.esquerda = removerRecursivo(atual.esquerda, id);
        } else if (id > idAtual) {                                      // id maior: desce pela direita
            atual.direita = removerRecursivo(atual.direita, id);
        } else {
            // Achou o nó a ser removido — trata os 3 casos:

            // CASO 1: nó folha (sem filhos) — apenas remove
            if (atual.esquerda == null && atual.direita == null) {
                return null;
            }

            // CASO 2: nó com apenas um filho — o filho sobe no lugar
            if (atual.esquerda == null) return atual.direita;
            if (atual.direita == null) return atual.esquerda;

            // CASO 3: nó com dois filhos
            // substitui o valor pelo menor nó da subárvore direita (sucessor em ordem)
            No sucessor = encontrarMenor(atual.direita);
            atual.os = sucessor.os;                                     // copia a OS do sucessor
            atual.direita = removerRecursivo(atual.direita, sucessor.os.getId()); // remove o sucessor lá embaixo
        }

        return atual;
    }

    // encontra o nó com menor id de uma subárvore (sempre o mais à esquerda)
    private No encontrarMenor(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    // ── Percursos ─────────────────────────────────────────────────────────────

    /**
     * Em Ordem (InOrder): esquerda → raiz → direita.
     * Retorna as OS ordenadas por id crescente.
     * Útil para listar todas as OS em ordem numérica.
     */
    public List<OrdemServicoModel> emOrdem() {
        List<OrdemServicoModel> resultado = new ArrayList<>();
        emOrdemRecursivo(raiz, resultado);
        return resultado;
    }

    private void emOrdemRecursivo(No atual, List<OrdemServicoModel> resultado) {
        if (atual == null) return;                  // caso base: nó vazio, para a recursão
        emOrdemRecursivo(atual.esquerda, resultado); // visita a subárvore esquerda primeiro
        resultado.add(atual.os);                     // visita a raiz
        emOrdemRecursivo(atual.direita, resultado);  // visita a subárvore direita por último
    }

    /**
     * Pré-Ordem (PreOrder): raiz → esquerda → direita.
     * A raiz sempre aparece antes dos filhos.
     * Útil para serializar ou clonar a estrutura da árvore.
     */
    public List<OrdemServicoModel> preOrdem() {
        List<OrdemServicoModel> resultado = new ArrayList<>();
        preOrdemRecursivo(raiz, resultado);
        return resultado;
    }

    private void preOrdemRecursivo(No atual, List<OrdemServicoModel> resultado) {
        if (atual == null) return;                   // caso base: nó vazio, para a recursão
        resultado.add(atual.os);                     // visita a raiz primeiro
        preOrdemRecursivo(atual.esquerda, resultado); // depois a subárvore esquerda
        preOrdemRecursivo(atual.direita, resultado);  // depois a subárvore direita
    }

    /**
     * Pós-Ordem (PostOrder): esquerda → direita → raiz.
     * A raiz sempre aparece depois dos filhos.
     * Útil para liberar memória ou calcular valores que dependem dos filhos.
     */
    public List<OrdemServicoModel> posOrdem() {
        List<OrdemServicoModel> resultado = new ArrayList<>();
        posOrdemRecursivo(raiz, resultado);
        return resultado;
    }

    private void posOrdemRecursivo(No atual, List<OrdemServicoModel> resultado) {
        if (atual == null) return;                   // caso base: nó vazio, para a recursão
        posOrdemRecursivo(atual.esquerda, resultado); // visita a subárvore esquerda primeiro
        posOrdemRecursivo(atual.direita, resultado);  // depois a subárvore direita
        resultado.add(atual.os);                      // visita a raiz por último
    }

    // ── Utilitários ───────────────────────────────────────────────────────────

    /** Retorna true se a árvore não contém nenhum nó. */
    public boolean estaVazia() {
        return raiz == null;
    }

    /** Retorna a quantidade total de nós na árvore. */
    public int contarNos() {
        return contarNosRecursivo(raiz);
    }

    private int contarNosRecursivo(No atual) {
        if (atual == null) return 0;                // nó vazio não conta
        return 1 + contarNosRecursivo(atual.esquerda) + contarNosRecursivo(atual.direita);
    }
}