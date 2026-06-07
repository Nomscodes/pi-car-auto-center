package br.com.picarauto.repository;

import br.com.picarauto.model.FornecedorModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para FornecedorModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IFornecedorRepository extends IGenericRepository<FornecedorModel> {

    boolean existsByCnpj(String cnpj);

    boolean existsByTelefone(String telefone);
}
