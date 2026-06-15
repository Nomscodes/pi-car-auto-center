package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Fila encadeada manual de Ordens de Serviço.
 *
 * Padrão de Projeto: Iterator
 * Implementa {@link Iterable} para expor os elementos via for-each sem
 * revelar os nós internos (classe {@code No}). A travessia é feita pela
 * classe interna {@link FilaOSIterator}.
 *
 * @author Caio4breu
 */
public class FilaOS implements Iterable<OrdemServicoModel> {
 
    private No frente;
    private No fim;
 
    private int tamanho;
 
    public FilaOS() {
        this.frente = null;
        this.fim = null;
        this.tamanho = 0;
    }
 
    public void enfileirar(OrdemServicoModel os) {
        if (os == null) {
            throw new IllegalArgumentException("Não é possível enfileirar uma OS nula.");
        }
 
        // Cria o novo nó com a OS recebida
        No novoNo = new No(os);
 
        if (estaVazia()) {
            frente = novoNo;
            fim = novoNo;
        } else {
            fim.proximo = novoNo;
            fim = novoNo;
        }
 
        tamanho++; 
    }
 
    public OrdemServicoModel desenfileirar() {
        if (estaVazia()) {
            throw new NoSuchElementException("Não é possível desenfileirar: a fila está vazia.");
        }
 
        OrdemServicoModel osDaFrente = frente.dado;
 
        frente = frente.proximo;
 
        if (frente == null) {
            fim = null;
        }
 
        tamanho--;
        return osDaFrente;
    }
 
    public OrdemServicoModel espiarFrente() {
        if (estaVazia()) {
            throw new NoSuchElementException("Não é possível espiar: a fila está vazia.");
        }
        return frente.dado;
    }
 
    public boolean estaVazia() {
        return tamanho == 0;
    }
 
    public int getTamanho() {
        return tamanho;
    }
 
    // PADRÃO ITERATOR
    @Override
    public Iterator<OrdemServicoModel> iterator() {
        return new FilaOSIterator(frente);
    }
 
    // CLASSE INTERNA: FilaOSIterator
    private static class FilaOSIterator implements Iterator<OrdemServicoModel> {
        private No atual;
 
        private FilaOSIterator(No inicio) {
            this.atual = inicio;
        }
 
        @Override
        public boolean hasNext() {
            return atual != null;
        }
 
        @Override
        public OrdemServicoModel next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Não há mais elementos na fila.");
            }
            OrdemServicoModel osAtual = atual.dado;
            atual = atual.proximo;
            return osAtual;
        }
    }
    
    // BUSCA SEQUENCIAL por ID da OS
    // A fila não garante nenhuma ordem — OS entram pela ordem de chegada na oficina.
    // Por isso não há como saber em qual posição está uma OS pelo seu id.
    // O único caminho é percorrer nó a nó do início ao fim até encontrar ou esgotar a fila.
    // Isso é, por definição, busca sequencial: O(n) no pior caso.
    // Retorna null se não encontrar.
    public OrdemServicoModel buscarSequencialPorId(Long id) {
        for (OrdemServicoModel os : this) {         // usa o Iterator interno da FilaOS
            if (os.getId() != null && os.getId().equals(id)) {
                return os;                          // encontrou — para imediatamente
            }
        }
        return null;                                // percorreu tudo e não achou
    }
    
    // Busca por placa do veículo — retorna todas as OS que combinam
    public java.util.List<OrdemServicoModel> buscarPorPlaca(String placa) {
        java.util.List<OrdemServicoModel> resultado = new java.util.ArrayList<>();
        if (placa == null || placa.isBlank()) return resultado;
        String placaNormalizada = placa.trim().toUpperCase();
        for (OrdemServicoModel os : this) {
            if (os.getPlacaVeiculo() != null
                    && os.getPlacaVeiculo().toUpperCase().contains(placaNormalizada)) {
                resultado.add(os);
            }
        }
        return resultado;
    }

    // Busca por nome do cliente — busca parcial, case-insensitive
    public java.util.List<OrdemServicoModel> buscarPorNomeCliente(String nomeCliente) {
        java.util.List<OrdemServicoModel> resultado = new java.util.ArrayList<>();
        if (nomeCliente == null || nomeCliente.isBlank()) return resultado;
        String nomeLower = nomeCliente.trim().toLowerCase();
        for (OrdemServicoModel os : this) {
            if (os.getNomeCliente() != null
                    && os.getNomeCliente().toLowerCase().contains(nomeLower)) {
                resultado.add(os);
            }
        }
        return resultado;
    }
}