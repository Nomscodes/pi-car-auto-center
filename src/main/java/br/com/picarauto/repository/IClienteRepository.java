package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;

/**
 *
 * @author Caio4breu
 */
public interface IClienteRepository extends IGenericRepository<ClienteModel> {
    boolean existsByCpf(String cpf);
}