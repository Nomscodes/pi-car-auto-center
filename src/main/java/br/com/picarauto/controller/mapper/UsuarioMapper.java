package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.dto.UsuarioDTO;

public class UsuarioMapper extends GenericMapper<UsuarioModel, UsuarioDTO> implements IUsuarioMapper {

    @Override
    public UsuarioDTO toDto(UsuarioModel entity) {
        if (entity == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNome(entity.getNome());
        dto.setLogin(entity.getLogin());
        dto.setEmail(entity.getEmail());
        dto.setPerfil(entity.getPerfil());
        return dto;
    }

    @Override
    public UsuarioModel toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        UsuarioModel entity = new UsuarioModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNome(dto.getNome());
        entity.setLogin(dto.getLogin());
        entity.setEmail(dto.getEmail());
        entity.setPerfil(dto.getPerfil());
        return entity;
    }
}