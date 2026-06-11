package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepository<E extends BaseModel> extends JpaRepository<E, Long> {
    Optional<E> findByIdAndAtivoTrue(Long id);
    List<E> findAllByAtivoTrue();
}