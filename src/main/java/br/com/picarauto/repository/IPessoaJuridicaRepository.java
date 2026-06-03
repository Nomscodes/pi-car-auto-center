package br.com.picarauto.repository;

import br.com.picarauto.model.PessoaJuridicaModel;

public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {
    boolean existsByCnpj(String cnpj);
    PessoaJuridicaModel findByCnpj(String cnpj);
}