package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PessoaJuridicaModel;

public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {
    boolean existsByCnpj(String cnpj);
    PessoaJuridicaModel findByCnpj(String cnpj);
}