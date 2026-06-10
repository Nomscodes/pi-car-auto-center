package br.com.picarauto.repository;

import br.com.picarauto.model.FornecedorModel;

/**
 * 
 * @author Gabriel
 */
public interface IFornecedorRepository extends IGenericRepository<FornecedorModel> {
    boolean existsByCnpj(String cnpj);
    boolean existsByTelefone(String telefone);
}
