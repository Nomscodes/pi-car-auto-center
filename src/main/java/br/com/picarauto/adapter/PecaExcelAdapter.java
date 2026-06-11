package br.com.picarauto.adapter;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.BusinessException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter de importação de peças a partir de planilha Excel (.xlsx).
 *
 * Padrão de Projeto: Adapter
 * Converte a interface incompatível do arquivo .xlsx (Apache POI)
 * para a interface esperada pelo sistema (List<PecaModel>),
 * sem modificar nem o PecaModel nem a planilha de origem.
 *
 * Formato esperado da planilha (linha 1 = cabeçalho, ignorada):
 * | A: codigoNacional | B: modelo | C: marca | D: anoVeiculo |
 * | E: anoModelo      | F: precoUnitario | G: garantia |
 *
 * O idFornecedor não é lido da planilha — deve ser vinculado
 * manualmente pelo atendente após a importação, na tela de peças.
 *
 * @author Cassiano
 */
public class PecaExcelAdapter implements IPecaExcelAdapter {

    @Override
    public List<PecaModel> importar(File arquivo) {
        List<PecaModel> pecas = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(arquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Lê sempre a primeira aba da planilha
            Sheet aba = workbook.getSheetAt(0);

            for (Row linha : aba) {

                // Pula o cabeçalho (linha 0)
                if (linha.getRowNum() == 0) continue;

                // Pula linhas completamente vazias
                if (linhaVazia(linha)) continue;

                PecaModel peca = new PecaModel();

                peca.setCodigoNacional(lerInteiro(linha.getCell(0)));
                peca.setModelo(lerTexto(linha.getCell(1)));
                peca.setMarca(lerTexto(linha.getCell(2)));
                peca.setAnoVeiculo(lerInteiro(linha.getCell(3)));
                peca.setAnoModelo(lerInteiro(linha.getCell(4)));
                peca.setPrecoUnitario(lerDecimal(linha.getCell(5)));
                peca.setGarantia(lerInteiro(linha.getCell(6)));

                // Só adiciona se os campos obrigatórios (not null no banco) vieram preenchidos.
                // Evita que uma linha com célula vazia ou malformada quebre o fluxo inteiro.
                if (peca.getCodigoNacional() != null
                        && peca.getModelo() != null
                        && peca.getMarca() != null) {
                    pecas.add(peca);
                }
            }

        } catch (Exception e) {
            throw new BusinessException(
                "Erro ao importar planilha de peças. Verifique se o arquivo está no formato correto (.xlsx).", e
            );
        }

        return pecas;
    }

    // ── Helpers de leitura de célula ──────────────────────────────────────────

    private String lerTexto(Cell celula) {
        if (celula == null) return null;
        return switch (celula.getCellType()) {
            case STRING  -> celula.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) celula.getNumericCellValue());
            default      -> null;
        };
    }

    private Integer lerInteiro(Cell celula) {
        if (celula == null) return null;
        return switch (celula.getCellType()) {
            case NUMERIC -> (int) celula.getNumericCellValue();
            case STRING  -> {
                try { yield Integer.parseInt(celula.getStringCellValue().trim()); }
                catch (NumberFormatException e) { yield null; }
            }
            default -> null;
        };
    }

    private double lerDecimal(Cell celula) {
        if (celula == null) return 0.0;
        return switch (celula.getCellType()) {
            case NUMERIC -> celula.getNumericCellValue();
            case STRING  -> {
                try { yield Double.parseDouble(celula.getStringCellValue().trim().replace(",", ".")); }
                catch (NumberFormatException e) { yield 0.0; }
            }
            default -> 0.0;
        };
    }

    private boolean linhaVazia(Row linha) {
        for (Cell celula : linha) {
            if (celula != null && celula.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}