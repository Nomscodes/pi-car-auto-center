package br.com.picarauto.service;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

/**
 * Service genérico — lógica de negócio reutilizável para todas as entidades.
 *
 * Padrão de Projeto: Template Method — define o fluxo de insert/update/delete
 * com hooks (beforeInsert, afterInsert...) que subclasses sobrescrevem.
 *
 * Com Spring Data, o repository é injetado via @Autowired nas subclasses;
 * passado aqui pelo construtor para manter o contrato genérico.
 */
public abstract class GenericService<
        E extends BaseModel,
        R extends IGenericRepository<E>,
        V extends IGenericValidation<E, R>>
        implements IGenericService<E, R, V> {

    protected R repository;
    protected V validation;

    public GenericService(R repository, V validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Override
    public E findByIdActive(Integer id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BusinessException("Registro não encontrado ou inativo."));
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
            E saved = repository.save(entity);
            afterInsert(saved, entity);
            return saved;
        } catch (BusinessException | FieldValidationException | RuleValidationException e) {
            throw e;
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
            E saved = repository.save(entity);
            afterUpdate(saved, entity);
            return saved;
        } catch (BusinessException | FieldValidationException | RuleValidationException e) {
            throw e;
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
        } catch (BusinessException | FieldValidationException | RuleValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Não foi possível excluir o registro em " + getEntityName(), e);
        }
    }

    protected String getEntityName() {
        return this.getClass().getSimpleName().replace("Service", "");
    }

    // Hooks — sobrescreva nas subclasses quando necessário
    protected void beforeInsert(E entity) {}
    protected void afterInsert(E entity, E old) {}
    protected void beforeUpdate(E entity) {}
    protected void afterUpdate(E entity, E old) {}
    protected void beforeDelete(E entity) {}
    protected void afterDelete(E entity) {}
}
