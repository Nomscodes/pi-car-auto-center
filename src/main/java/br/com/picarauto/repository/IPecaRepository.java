package br.com.picarauto.repository;

import br.com.picarauto.model.PecaModel;

public interface IPecaRepository extends IGenericRepository<PecaModel> {

    boolean existsByNome(String nome);
}