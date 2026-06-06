package br.com.picarauto.repository;

import br.com.picarauto.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de repositório.
 * Estende JpaRepository — o Spring Data gera a implementação automaticamente.
 *
 * @NoRepositoryBean: indica ao Spring que esta interface não deve ser
 * instanciada diretamente; apenas as interfaces filhas são beans.
 *
 * @param <E> Tipo da entidade (deve estender BaseModel)
 */
@NoRepositoryBean
public interface IGenericRepository<E extends BaseModel> extends JpaRepository<E, Integer> {

    // Métodos herdados do JpaRepository (não precisam ser declarados):
    // save(entity), findById(id), findAll(), deleteById(id), existsById(id), etc.

    // Métodos de soft delete por convenção Spring Data (gerados automaticamente por nome):
    List<E> findAllByAtivoTrue();
    Optional<E> findByIdAndAtivoTrue(Integer id);
}
