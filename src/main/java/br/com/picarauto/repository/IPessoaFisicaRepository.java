package br.com.picarauto.repository;

import br.com.picarauto.model.PessoaFisicaModel;

public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {
    boolean existsByCpf(String cpf);
    PessoaFisicaModel findByCpf(String cpf);
}