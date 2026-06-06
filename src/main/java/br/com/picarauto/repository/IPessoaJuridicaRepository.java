package br.com.picarauto.repository;

import br.com.picarauto.model.PessoaJuridicaModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para PessoaJuridicaModel.
 * O Spring gera a implementação automaticamente em tempo de execução.
 */
@Repository
public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {
}
