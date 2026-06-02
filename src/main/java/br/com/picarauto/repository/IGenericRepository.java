package br.com.picarauto.repository;

import br.com.picarauto.model.BaseModel;
import java.util.List;

public interface IGenericRepository<E extends BaseModel> {
    E findByIdAndAtivoTrue(Integer id);
    List<E> findAllByAtivoTrue();
    E save(E entity);
    boolean existsById(Integer id);
}