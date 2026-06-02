package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericMapper<E extends BaseModel, D extends BaseDTO>
        implements IGenericMapper<E, D> {

    @Override
    public abstract D toDto(E entity);

    @Override
    public abstract E toEntity(D dto);

    @Override
    public List<D> toDtoList(List<E> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}