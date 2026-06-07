package br.com.picarauto.repository;

/**
 * Repository para a tabela de relacionamento {@code itemFornecedor}.
 *
 * Vincula um fornecedor ({@code fornecedor}) ao serviço externo prestado
 * ({@code itemPedidoServicoExterno}), registrando a data de execução.
 *
 * Chave composta: idFornecedor + idItemPedidoServicoExterno + dataExecucao
 * Não estende IGenericRepository pois a tabela não possui PK serial nem coluna ativo.
 *
 * @author Caio4breu
 */
import java.time.LocalDate;
import java.util.List;

public interface IItemFornecedorRepository {
    void save(Long idFornecedor, Long idItemPedidoServicoExterno, LocalDate dataExecucao);
    List<Long> findIdItemByIdFornecedor(Long idFornecedor);
    List<Long> findIdFornecedorByIdItem(Long idItemPedidoServicoExterno);
    boolean existsByFornecedorAndItem(Long idFornecedor, Long idItemPedidoServicoExterno);
    void delete(Long idFornecedor, Long idItemPedidoServicoExterno, LocalDate dataExecucao);
}