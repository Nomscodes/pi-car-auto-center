package br.com.picarauto.repository;

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
import br.com.picarauto.model.ItemServicoInternoModel;
import java.time.LocalDate;
import java.util.List;

public interface IServicosItensRepository {
    void save(Long idServicoInterno, Long idItemServicoInterno, LocalDate dataExecucao);
    List<ItemServicoInternoModel> findAllByIdServicoInterno(Long idServicoInterno);
    List<Long> findIdServicoInternoByIdItemServicoInterno(Long idItemServicoInterno);
    boolean existsByServicoInternoAndItemServicoInterno(Long idServicoInterno, Long idItemServicoInterno);
    void delete(Long idServicoInterno, Long idItemServicoInterno);
}