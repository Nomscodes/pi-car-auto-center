package br.com.picarauto.service;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericService<E extends BaseModel, R extends IGenericRepository<E>, V extends IGenericValidation<E, R>>
        implements IGenericService<E, R, V> {

    @Autowired
    protected R repository;

    @Autowired
    protected V validation;

    @Override
    @Transactional(readOnly = true)
    public E findByIdActive(Long id) {
        try {
            return repository.findByIdAndAtivoTrue(id)
                    .orElseThrow(() -> new BusinessException("O registro com o ID informado não foi encontrado ou está inativo."));
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Erro ao localizar o registro em " + getEntityName(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAllActive() {
        try {
            return repository.findAllByAtivoTrue();
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar registros em " + getEntityName(), e);
        }
    }

    @Override
    @Transactional
    public E insert(E entity) {
        try {
            validation.validateFieldsInsert(entity);
            validation.validateInsert(entity);
            beforeInsert(entity);
            E savedEntity = repository.save(entity);
            afterInsert(savedEntity, entity);
            return savedEntity;
        } catch (BusinessException | FieldValidationException | RuleValidationException be) {
            throw be;
        } catch (DataAccessException dae) {
            throw new BusinessException("Falha de persistência ao inserir " + getEntityName(), dae);
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao inserir em " + getEntityName(), e);
        }
    }

    @Override
    @Transactional
    public E update(E entity) {
        try {
            validation.validateFieldsUpdate(entity);
            validation.validateUpdate(entity);
            beforeUpdate(entity);
            E savedEntity = repository.save(entity);
            afterUpdate(savedEntity, entity);
            return savedEntity;
        } catch (BusinessException | FieldValidationException | RuleValidationException be) {
            throw be;
        } catch (DataAccessException dae) {
            throw new BusinessException("Erro de integridade ao atualizar " + getEntityName(), dae);
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao atualizar em " + getEntityName(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            validation.validateDelete(id);
            E entity = findByIdActive(id);
            beforeDelete(entity);
            entity.setAtivo(false);
            repository.save(entity);
            afterDelete(entity);
        } catch (BusinessException | FieldValidationException | RuleValidationException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Não foi possível excluir o registro em " + getEntityName(), e);
        }
    }

    protected String getEntityName() {
        return this.getClass().getSimpleName().replace("Service", "");
    }

    // Hooks
    protected void beforeInsert(E entity) {}
    protected void afterInsert(E entity, E old) {}
    protected void beforeUpdate(E entity) {}
    protected void afterUpdate(E entity, E old) {}
    protected void beforeDelete(E entity) {}
    protected void afterDelete(E entity) {}
}