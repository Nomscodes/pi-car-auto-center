package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PessoaFisicaModel;

public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {
    boolean existsByCpf(String cpf);
    PessoaFisicaModel findByCpf(String cpf);
}