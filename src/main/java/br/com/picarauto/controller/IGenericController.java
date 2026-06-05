package br.com.picarauto.controller;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

/**
 * Interface genérica do controller adaptada para desktop (sem Spring, sem HTTP).
 * Opera diretamente com entidades — a conversão para DTO é responsabilidade da view.
 *
 * E: Entity (BaseModel)
 * S: Service especializado
 * @Author Caio4breu
 */
public interface IGenericController<
        E extends BaseModel,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>> {

    E findById(Integer id);
    List<E> findAll();
    E insert(E entity);
    E update(E entity);
    void delete(Integer id);
}