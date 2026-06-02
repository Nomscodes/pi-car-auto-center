package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IGenericMapper;
import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import br.com.picarauto.repository.IGenericRepository;
import br.com.picarauto.service.IGenericService;
import br.com.picarauto.validation.IGenericValidation;
import java.util.List;

/**
 * Interface genérica do controller adaptada para desktop (sem Spring, sem HTTP).
 * Opera diretamente com entidades e DTOs, servindo de contrato entre a
 * camada de tela (Swing) e a camada de serviço.
 *
 * E: Entity (BaseModel)
 * D: DTO    (BaseDTO)
 * S: Service especializado
 * M: Mapper especializado
 */
public interface IGenericController<
        E extends BaseModel,
        D extends BaseDTO,
        S extends IGenericService<E, ? extends IGenericRepository<E>, ? extends IGenericValidation<E, ? extends IGenericRepository<E>>>,
        M extends IGenericMapper<E, D>> {

    D findById(Integer id);

    List<D> findAll();

    D insert(E entity);

    D update(E entity);

    void delete(Integer id);
}