package br.com.picarauto.adapter;

import br.com.picarauto.model.exception.BusinessException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Adapter de geração de relatórios em PDF usando Apache PDFBox.
 *
 * Padrão de Projeto: Adapter
 * Converte a interface incompatível do Apache PDFBox (PDDocument,
 * PDPageContentStream, desenho de texto por coordenadas x/y, controle
 * manual de fontes e de quebra de página) para a interface simples que
 * as telas do sistema esperam:
 * IRelatorioPdfAdapter.gerar(titulo, colunas, linhas, arquivoDestino).
 *
 * A tela apenas fornece os dados já formatados (título, cabeçalho e
 * linhas como String[]) e recebe de volta o File do PDF gerado — sem
 * precisar conhecer nada da API do PDFBox.
 *
 * @author Cassiano
 */
public class RelatorioPdfAdapter implements IRelatorioPdfAdapter {

    private static final float MARGEM        = 40f;
    private static final float ALTURA_LINHA  = 18f;
    private static final float TAM_FONTE_TIT = 14f;
    private static final float TAM_FONTE     = 9f;

    private static final PDFont FONTE_NORMAL = PDType1Font.HELVETICA;
    private static final PDFont FONTE_NEGRITO = PDType1Font.HELVETICA_BOLD;

    @Override
    public File gerar(String titulo, String[] colunas, List<String[]> linhas, File arquivoDestino) {
        try (PDDocument documento = new PDDocument()) {

            float larguraPagina = PDRectangle.A4.getWidth();
            float alturaPagina  = PDRectangle.A4.getHeight();
            float larguraUtil   = larguraPagina - (2 * MARGEM);
            float[] larguraColunas = calcularLarguraColunas(colunas, larguraUtil);

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);
            PDPageContentStream conteudo = new PDPageContentStream(documento, pagina);

            float y = alturaPagina - MARGEM;
            y = escreverTitulo(conteudo, titulo, y);
            y = escreverCabecalho(conteudo, colunas, larguraColunas, y);

            for (String[] linha : linhas) {
                // Quebra de página: se não houver espaço para mais uma linha, abre nova página
                if (y < MARGEM + ALTURA_LINHA) {
                    conteudo.close();
                    pagina = new PDPage(PDRectangle.A4);
                    documento.addPage(pagina);
                    conteudo = new PDPageContentStream(documento, pagina);
                    y = alturaPagina - MARGEM;
                    y = escreverCabecalho(conteudo, colunas, larguraColunas, y);
                }
                y = escreverLinha(conteudo, linha, larguraColunas, y);
            }

            conteudo.close();
            documento.save(arquivoDestino);
            return arquivoDestino;

        } catch (IOException e) {
            throw new BusinessException("Erro ao gerar o relatório em PDF.", e);
        }
    }

    // ── Helpers de montagem do PDF ────────────────────────────────────────────

    private float escreverTitulo(PDPageContentStream conteudo, String titulo, float y) throws IOException {
        conteudo.beginText();
        conteudo.setFont(FONTE_NEGRITO, TAM_FONTE_TIT);
        conteudo.newLineAtOffset(MARGEM, y);
        conteudo.showText(titulo);
        conteudo.endText();
        y -= ALTURA_LINHA;

        String dataGeracao = "Gerado em " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        conteudo.beginText();
        conteudo.setFont(FONTE_NORMAL, TAM_FONTE - 1);
        conteudo.newLineAtOffset(MARGEM, y);
        conteudo.showText(dataGeracao);
        conteudo.endText();

        return y - (ALTURA_LINHA * 1.5f);
    }

    private float escreverCabecalho(PDPageContentStream conteudo, String[] colunas, float[] larguraColunas, float y) throws IOException {
        float x = MARGEM;
        conteudo.setFont(FONTE_NEGRITO, TAM_FONTE);
        for (int i = 0; i < colunas.length; i++) {
            conteudo.beginText();
            conteudo.newLineAtOffset(x, y);
            conteudo.showText(truncar(colunas[i], larguraColunas[i], FONTE_NEGRITO, TAM_FONTE));
            conteudo.endText();
            x += larguraColunas[i];
        }
        y -= 4f;
        conteudo.setLineWidth(0.5f);
        conteudo.moveTo(MARGEM, y);
        conteudo.lineTo(MARGEM + somar(larguraColunas), y);
        conteudo.stroke();

        return y - ALTURA_LINHA;
    }

    private float escreverLinha(PDPageContentStream conteudo, String[] linha, float[] larguraColunas, float y) throws IOException {
        float x = MARGEM;
        conteudo.setFont(FONTE_NORMAL, TAM_FONTE);
        for (int i = 0; i < linha.length && i < larguraColunas.length; i++) {
            String valor = linha[i] != null ? linha[i] : "-";
            conteudo.beginText();
            conteudo.newLineAtOffset(x, y);
            conteudo.showText(truncar(valor, larguraColunas[i], FONTE_NORMAL, TAM_FONTE));
            conteudo.endText();
            x += larguraColunas[i];
        }
        return y - ALTURA_LINHA;
    }

    // ── Helpers de layout ──────────────────────────────────────────────────────

    private float[] calcularLarguraColunas(String[] colunas, float larguraUtil) {
        float[] larguras = new float[colunas.length];
        float largura = larguraUtil / colunas.length;
        for (int i = 0; i < colunas.length; i++) {
            larguras[i] = largura;
        }
        return larguras;
    }

    private float somar(float[] valores) {
        float total = 0;
        for (float v : valores) total += v;
        return total;
    }

    // Corta o texto com "..." caso ele não caiba na largura da coluna
    private String truncar(String texto, float larguraColuna, PDFont fonte, float tamanhoFonte) throws IOException {
        if (texto == null) return "-";
        float margemInterna = 4f;
        float larguraMaxima = larguraColuna - margemInterna;

        if (fonte.getStringWidth(texto) / 1000 * tamanhoFonte <= larguraMaxima) {
            return texto;
        }

        String reticencias = "...";
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            String tentativa = sb.toString() + c + reticencias;
            if (fonte.getStringWidth(tentativa) / 1000 * tamanhoFonte > larguraMaxima) {
                break;
            }
            sb.append(c);
        }
        return sb + reticencias;
    }
}