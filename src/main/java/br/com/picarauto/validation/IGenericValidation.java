package br.com.picarauto.validation;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.repository.IGenericRepository;

/**
 * 
 * @author Caio4breu
 */
public interface IGenericValidation<E extends BaseModel, R extends IGenericRepository<E>> {
    void validateFields(E entity);
    default void validateFieldsInsert(E entity) {}
    default void validateFieldsUpdate(E entity) {}
    default void validateInsert(E entity) {}
    default void validateUpdate(E entity) {}
    default void validateDelete(Integer id) {}
}