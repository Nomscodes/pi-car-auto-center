package br.com.picarauto.util;

import br.com.picarauto.model.OrdemServicoModel;
import java.text.Collator;
import java.util.Locale;

/**
 * Ordena as OS pelo nome do cliente de A a Z (ASC) ou de Z a A (DESC).
 *
 * Padrão de Projeto: Template Method
 * Subclasse de {@link OrdenadorOS} — implementa apenas o critério de
 * comparação. O algoritmo de ordenação em si está na classe pai.
 *
 * O campo {@code nomeCliente} é {@code @Transient} — deve ser populado
 * antes de enfileirar a OS para que esta ordenação funcione corretamente.
 *
 * Usa {@link Collator} com {@code Locale.forLanguageTag("pt-BR")} para
 * tratar acentuação corretamente: "Ábner" vem antes de "Adriana", não depois.
 *
 * Use {@code ordenar(fila)} para A–Z
 * ou {@code ordenar(fila, Direcao.DESC)} para Z–A.
 *
 * @author Caio4breu
 */
public class OrdenadorPorNomeCliente extends OrdenadorOS {

    // Collator garante ordenação alfabética correta para o português —
    // letras acentuadas (ã, é, ç) são tratadas como variantes das suas
    // letras base, não como caracteres fora do alfabeto.
    private static final Collator COLLATOR;
    
    static {
        // Optamos pelo Insertion Sort em vez do Bubble Sort ou Selection Sort,
        // algoritmos vistos em aula, pois ambos são reconhecidamente ineficientes
        // para uso em produção. O Insertion Sort cumpre o mesmo propósito acadêmico
        // de ordenação manual sem uso de bibliotecas, com a vantagem de ser
        // significativamente mais eficiente em listas pequenas e parcialmente
        // ordenadas — exatamente o perfil de uma fila de OS de oficina.
        COLLATOR = Collator.getInstance(Locale.forLanguageTag("pt-BR"));
        COLLATOR.setStrength(Collator.SECONDARY); // ignora maiúsculas/minúsculas, respeita acentos
    }

    /**
     * Compara duas OS pelo nome do cliente, ignorando maiúsculas/minúsculas
     * e respeitando acentuação do português.
     * OS com nome nulo são enviadas para o fim da lista.
     */
    @Override
    protected int comparar(OrdemServicoModel a, OrdemServicoModel b) {
        if (a.getNomeCliente() == null && b.getNomeCliente() == null) return 0;
        if (a.getNomeCliente() == null) return 1;
        if (b.getNomeCliente() == null) return -1;
        return COLLATOR.compare(a.getNomeCliente(), b.getNomeCliente());
    }
}