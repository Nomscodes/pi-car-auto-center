package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IGenericRepository;

public abstract class GenericValidation<E extends BaseModel, R extends IGenericRepository<E>>
        implements IGenericValidation<E, R> {

    protected final R repository;

    protected GenericValidation(R repository) {
        this.repository = repository;
    }

    @Override
    public void validateFields(E entity) {
        if (entity == null) {
            throw new FieldValidationException("root", "A entidade enviada não pode ser nula.");
        }
    }

    @Override
    public void validateFieldsInsert(E entity) {
        validateFields(entity);
        if (entity.getId() != null) {
            throw new FieldValidationException("id", "O ID deve ser nulo para novas inserções.");
        }
    }

    @Override
    public void validateFieldsUpdate(E entity) {
        validateFields(entity);
        if (entity.getId() == null) {
            throw new FieldValidationException("id", "O ID é obrigatório para atualizações.");
        }
    }

    @Override
    public void validateDelete(Long id) {
        if (id == null) {
            throw new FieldValidationException("id", "ID de exclusão inválido.");
        }
    }

    @Override
    public void validateInsert(E entity) {}

    @Override
    public void validateUpdate(E entity) {
        if (!repository.existsById(entity.getId())) {
            throw new RuleValidationException("id", "O registro com o ID informado não existe.");
        }
    }
}