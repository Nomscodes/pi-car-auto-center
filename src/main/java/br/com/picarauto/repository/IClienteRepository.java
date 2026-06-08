package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ClienteModel;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepository extends IGenericRepository<ClienteModel> {
    Optional<ClienteModel> findByIdAndAtivoTrue(Long id);
}