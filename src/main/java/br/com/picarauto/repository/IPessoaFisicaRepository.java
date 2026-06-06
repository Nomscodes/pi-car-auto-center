package br.com.picarauto.repository;

import br.com.picarauto.model.PessoaFisicaModel;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório Spring Data para PessoaFisicaModel.
 */
@Repository
public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {
    boolean existsByCpf(String cpf);
    Optional<PessoaFisicaModel> findByCpf(String cpf);
}
