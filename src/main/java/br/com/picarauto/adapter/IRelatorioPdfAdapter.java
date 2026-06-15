package br.com.picarauto.adapter;

import java.io.File;
import java.util.List;

/**
 * Contrato do Adapter de geração de relatórios em PDF.
 *
 * Padrão de Projeto: Adapter
 * Define a interface que as telas do sistema usam para gerar um relatório
 * em PDF a partir de dados tabulares (título + cabeçalho + linhas).
 * As telas conhecem apenas este contrato — não conhecem a biblioteca
 * concreta usada para montar o PDF (PDDocument, PDPageContentStream,
 * coordenadas x/y, fontes etc).
 *
 * @author Caio4breu
 */
public interface IRelatorioPdfAdapter {

    /**
     * Gera um arquivo PDF tabular a partir dos dados informados.
     *
     * @param titulo         título exibido no topo do relatório
     * @param colunas        nomes das colunas (cabeçalho da tabela)
     * @param linhas         linhas de dados, já formatadas como texto
     * @param arquivoDestino arquivo .pdf onde o relatório será salvo
     * @return o próprio arquivo gerado, para facilitar abrir/exibir em seguida
     */
    File gerar(String titulo, String[] colunas, List<String[]> linhas, File arquivoDestino);
}