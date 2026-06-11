package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.repository.IGenericRepository;

public interface IGenericValidation<E extends BaseModel, R extends IGenericRepository<E>> {
    // Validações que ocorrem SEMPRE (Insert e Update)
    void validateFields(E entity);

    // Validações de campos específicas para NOVOS registros
    default void validateFieldsInsert(E entity) {}

    // Validações de campos específicas para ATUALIZAÇÃO
    default void validateFieldsUpdate(E entity) {}

    // Regras de negócio (Contextuais)
    default void validateInsert(E entity) {}
    default void validateUpdate(E entity) {}
    default void validateDelete(Long id) {}
}