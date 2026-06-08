package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.PessoaJuridicaModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaJuridicaRepository extends IGenericRepository<PessoaJuridicaModel> {
    boolean existsByCnpj(String cnpj);
    Optional<PessoaJuridicaModel> findByCnpj(String cnpj);
}