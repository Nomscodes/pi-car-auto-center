package br.com.picarauto.repository;

import java.time.LocalDate;
import java.util.List;

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
public interface IItemFornecedorRepository {

    // Vincula um fornecedor a um item de serviço externo
    void save(Integer idFornecedor, Integer idItemPedidoServicoExterno, LocalDate dataExecucao);

    // Retorna os IDs de itens de serviço externo executados por um fornecedor
    List<Integer> findIdItemByIdFornecedor(Integer idFornecedor);

    // Retorna os IDs de fornecedores que executaram um item de serviço externo
    List<Integer> findIdFornecedorByIdItem(Integer idItemPedidoServicoExterno);

    // Verifica se o vínculo já existe
    boolean existsByFornecedorAndItem(Integer idFornecedor, Integer idItemPedidoServicoExterno);

    // Remove o vínculo
    void delete(Integer idFornecedor, Integer idItemPedidoServicoExterno, LocalDate dataExecucao);
}