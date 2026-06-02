package br.com.picarauto.service;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

public interface IGenericService<E extends BaseModel, R extends IGenericRepository<E>, V extends IGenericValidation<E, R>> {

    E findByIdActive(Integer id);
    List<E> findAllActive();
    E insert(E entity);
    E update(E entity);
    void delete(Integer id);
}