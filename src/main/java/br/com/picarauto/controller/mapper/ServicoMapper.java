package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.model.dto.ServicoDTO;

public class ServicoMapper extends GenericMapper<ServicoModel, ServicoDTO> implements IServicoMapper {

    @Override
    public ServicoDTO toDto(ServicoModel entity) {
        if (entity == null) return null;
        ServicoDTO dto = new ServicoDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setPreco(entity.getPreco());
        return dto;
    }

    @Override
    public ServicoModel toEntity(ServicoDTO dto) {
        if (dto == null) return null;
        ServicoModel entity = new ServicoModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        return entity;
    }
}