package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PessoaFisicaModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaFisicaRepository extends IGenericRepository<PessoaFisicaModel> {
    boolean existsByCpf(String cpf);
    Optional<PessoaFisicaModel> findByCpf(String cpf);
}