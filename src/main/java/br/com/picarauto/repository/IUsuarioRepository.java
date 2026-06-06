package br.com.picarauto.repository;

import br.com.picarauto.model.UsuarioModel;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositório Spring Data para UsuarioModel.
 */
@Repository
public interface IUsuarioRepository extends IGenericRepository<UsuarioModel> {
    Optional<UsuarioModel> findByLogin(String login);
    boolean existsByLogin(String login);
}
