package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
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
}