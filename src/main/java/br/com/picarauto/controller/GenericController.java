package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IGenericMapper;
import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

/**
 * Controller genérico adaptado para desktop (sem Spring, sem HTTP).
 * Faz a ponte entre a tela Swing e o service, convertendo entidades em DTOs.
 * Cada domínio cria seu próprio controller estendendo esta classe.
 *
 * E: Entity (BaseModel)
 * D: DTO    (BaseDTO)
 * S: Service especializado
 * M: Mapper especializado
 */
public abstract class GenericController<
        E extends BaseModel,
        D extends BaseDTO,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>,
        M extends IGenericMapper<E, D>>
        implements IGenericController<E, D, S, M> {

    protected final S service;
    protected final M mapper;

    public GenericController(S service, M mapper) {
        this.service = service;
        this.mapper  = mapper;
    }

    /** Busca um registro ativo pelo ID e retorna como DTO. */
    @Override
    public D findById(Integer id) {
        E entity = service.findByIdActive(id);
        return mapper.toDto(entity);
    }

    /** Retorna todos os registros ativos como lista de DTOs. */
    @Override
    public List<D> findAll() {
        return mapper.toDtoList(service.findAllActive());
    }

    /** Valida, persiste e retorna o DTO do registro inserido. */
    @Override
    public D insert(E entity) {
        E saved = service.insert(entity);
        return mapper.toDto(saved);
    }

    /** Valida, atualiza e retorna o DTO do registro alterado. */
    @Override
    public D update(E entity) {
        E updated = service.update(entity);
        return mapper.toDto(updated);
    }

    /** Realiza soft delete (ativo = false) pelo ID. */
    @Override
    public void delete(Integer id) {
        service.delete(id);
    }
}