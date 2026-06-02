package br.com.picarauto.repository;

import br.com.picarauto.model.MecanicoModel;

public interface IMecanicoRepository extends IGenericRepository<MecanicoModel> {
    boolean existsByCpf(String cpf);
}