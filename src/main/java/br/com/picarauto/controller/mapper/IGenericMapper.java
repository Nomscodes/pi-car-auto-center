package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.BaseModel;
import br.com.picarauto.model.dto.BaseDTO;
import java.util.List;
import java.util.stream.Collectors;

public interface IGenericMapper<E extends BaseModel, D extends BaseDTO> {

    D toDto(E entity);
    E toEntity(D dto);

    default List<D> toDtoList(List<E> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
}