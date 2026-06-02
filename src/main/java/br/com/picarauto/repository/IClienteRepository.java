package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;

public interface IClienteRepository extends IGenericRepository<ClienteModel> {
    boolean existsByCpf(String cpf);
}