package br.com.picarauto.adapter;

import br.com.picarauto.model.PecaModel;
import java.io.File;
import java.util.List;

/**
 * Contrato do Adapter de importação de peças via planilha Excel.
 *
 * Padrão de Projeto: Adapter
 * Define a interface esperada pelo sistema para receber dados externos
 * de uma planilha .xlsx. Qualquer implementação concreta precisa respeitar
 * este contrato, independente do formato ou estrutura do arquivo de origem.
 *
 * @author Cassiano
 */
public interface IPecaExcelAdapter {

    /**
     * Lê uma planilha .xlsx e converte cada linha em um PecaModel.
     *
     * @param arquivo Arquivo .xlsx enviado pelo usuário
     * @return Lista de PecaModel prontos para serem persistidos via PecaService
     */
    List<PecaModel> importar(File arquivo);
}
