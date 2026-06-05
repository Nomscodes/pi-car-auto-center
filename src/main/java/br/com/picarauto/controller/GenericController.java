package br.com.picarauto.controller;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

/**
 * Controller genérico adaptado para desktop (sem Spring, sem HTTP).
 * Faz a ponte entre a tela Swing e o service.
 * A conversão para DTO é responsabilidade da view, que conhece o contexto da tela.
 *
 * E: Entity (BaseModel)
 * S: Service especializado
 * @Author Caio4breu
 */
public abstract class GenericController<
        E extends BaseModel,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>>
        implements IGenericController<E, S> {

    protected final S service;

    public GenericController(S service) {
        this.service = service;
    }

    /** Busca um registro ativo pelo ID. */
    @Override
    public E findById(Integer id) {
        return service.findByIdActive(id);
    }

    /** Retorna todos os registros ativos. */
    @Override
    public List<E> findAll() {
        return service.findAllActive();
    }

    /** Valida e persiste o registro. */
    @Override
    public E insert(E entity) {
        return service.insert(entity);
    }

    /** Valida e atualiza o registro. */
    @Override
    public E update(E entity) {
        return service.update(entity);
    }

    /** Realiza soft delete (ativo = false) pelo ID. */
    @Override
    public void delete(Integer id) {
        service.delete(id);
    }
}