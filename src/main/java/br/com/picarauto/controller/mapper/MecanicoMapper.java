package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.dto.MecanicoDTO;

public class MecanicoMapper extends GenericMapper<MecanicoModel, MecanicoDTO> implements IMecanicoMapper {

    @Override
    public MecanicoDTO toDto(MecanicoModel entity) {
        if (entity == null) return null;
        MecanicoDTO dto = new MecanicoDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setEspecialidade(entity.getEspecialidade());
        dto.setCrea(entity.getCrea());
        return dto;
    }

    @Override
    public MecanicoModel toEntity(MecanicoDTO dto) {
        if (dto == null) return null;
        MecanicoModel entity = new MecanicoModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setEspecialidade(dto.getEspecialidade());
        entity.setCrea(dto.getCrea());
        return entity;
    }
}