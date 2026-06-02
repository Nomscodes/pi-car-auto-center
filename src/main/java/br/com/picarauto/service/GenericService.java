package br.com.picarauto.service;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

public abstract class GenericService<E extends BaseModel, R extends IGenericRepository<E>, V extends IGenericValidation<E, R>>
        implements IGenericService<E, R, V> {

    protected R repository;
    protected V validation;

    public GenericService(R repository, V validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Override
    public E findByIdActive(Integer id) {
        try {
            E entity = repository.findByIdAndAtivoTrue(id);
            if (entity == null) {
                throw new BusinessException("Registro não encontrado ou inativo.");
            }
            return entity;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Erro ao localizar o registro em " + getEntityName(), e);
        }
    }

    @Override
    public List<E> findAllActive() {
        try {
            return repository.findAllByAtivoTrue();
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar registros em " + getEntityName(), e);
        }
    }

    @Override
    public E insert(E entity) {
        try {
            validation.validateFieldsInsert(entity);
            validation.validateInsert(entity);
            entity.onCreate();
            beforeInsert(entity);

            E savedEntity = repository.save(entity);

            afterInsert(savedEntity, entity);
            return savedEntity;
        } catch (BusinessException | FieldValidationException | RuleValidationException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao inserir em " + getEntityName(), e);
        }
    }

    @Override
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
        } catch (Exception e) {
            throw new BusinessException("Erro inesperado ao atualizar em " + getEntityName(), e);
        }
    }

    @Override
    public void delete(Integer id) {
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