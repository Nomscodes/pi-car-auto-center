package br.com.picarauto.controller;

/**
 * Controller genérico adaptado para desktop (sem HTTP).
 * Faz a ponte entre a tela Swing e o service.
 * A conversão para DTO é responsabilidade da view, que conhece o contexto da tela.
 *
 * E: Entity (BaseModel)
 * S: Service especializado
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

public abstract class GenericController<
        E extends BaseModel,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>>
        implements IGenericController<E, S> {

    protected final S service;

    public GenericController(S service) {
        this.service = service;
    }

    @Override
    public E findById(Long id) {
        return service.findByIdActive(id);
    }

    @Override
    public List<E> findAll() {
        return service.findAllActive();
    }

    @Override
    public E insert(E entity) {
        return service.insert(entity);
    }

    @Override
    public E update(E entity) {
        return service.update(entity);
    }

    @Override
    public void delete(Long id) {
        service.delete(id);
    }
}