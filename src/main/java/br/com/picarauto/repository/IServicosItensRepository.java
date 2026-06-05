package br.com.picarauto.repository;

import br.com.picarauto.model.ItemServicoInternoModel;
import java.util.List;

/**
 * Repository para a tabela de relacionamento {@code servicosItens}.
 *
 * Vincula um item de serviço interno ({@code itemServicoInterno})
 * ao seu respectivo serviço do catálogo ({@code servicosInternos}).
 *
 * Chave composta: idServicoInterno + idItemServicoInterno
 * Não estende IGenericRepository pois a tabela não possui PK serial nem coluna ativo.
 *
 * @author Caio4breu
 */
public interface IServicosItensRepository {

    // Vincula um serviço interno a um item de OS
    void save(Integer idServicoInterno, Integer idItemServicoInterno, java.time.LocalDate dataExecucao);

    // Retorna todos os itens vinculados a um serviço interno do catálogo
    List<ItemServicoInternoModel> findAllByIdServicoInterno(Integer idServicoInterno);

    // Retorna todos os serviços do catálogo vinculados a um item de OS
    List<Integer> findIdServicoInternoByIdItemServicoInterno(Integer idItemServicoInterno);

    // Verifica se o vínculo já existe
    boolean existsByServicoInternoAndItemServicoInterno(Integer idServicoInterno, Integer idItemServicoInterno);

    // Remove o vínculo entre um serviço interno e um item de OS
    void delete(Integer idServicoInterno, Integer idItemServicoInterno);
}