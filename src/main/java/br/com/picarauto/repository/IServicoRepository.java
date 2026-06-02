package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoModel;

public interface IServicoRepository extends IGenericRepository<ServicoModel> {
    boolean existsByNome(String nome);
}