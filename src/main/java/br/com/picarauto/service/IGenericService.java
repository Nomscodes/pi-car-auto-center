package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

public interface IGenericService<E extends BaseModel, R extends IGenericRepository<E>, V extends IGenericValidation<E, R>> {
    E findByIdActive(Long id);
    List<E> findAllActive();
    E insert(E entity);
    E update(E entity);
    void delete(Long id);
}