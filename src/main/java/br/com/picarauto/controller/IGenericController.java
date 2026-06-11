package br.com.picarauto.controller;

/**
 * Interface genérica do controller adaptada para desktop (sem HTTP).
 * Opera diretamente com entidades — a conversão para DTO é responsabilidade da view.
 *
 * E: Entity (BaseModel)
 * S: Service especializado
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

public interface IGenericController<
        E extends BaseModel,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>> {

    E findById(Long id);
    List<E> findAll();
    E insert(E entity);
    E update(E entity);
    void delete(Long id);
}