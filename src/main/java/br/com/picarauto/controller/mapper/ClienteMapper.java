package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.dto.ClienteDTO;

public class ClienteMapper extends GenericMapper<ClienteModel, ClienteDTO> implements IClienteMapper {

    @Override
    public ClienteDTO toDto(ClienteModel entity) {
        if (entity == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNomeCompleto(entity.getNomeCompleto());
        dto.setTelefone(entity.getTelefone());
        dto.setEmail(entity.getEmail());
        dto.setEndereco(entity.getEndereco());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }

    @Override
    public ClienteModel toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        ClienteModel entity = new ClienteModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNomeCompleto(dto.getNomeCompleto());
        entity.setTelefone(dto.getTelefone());
        entity.setEmail(dto.getEmail());
        entity.setEndereco(dto.getEndereco());
        entity.setDataCadastro(dto.getDataCadastro());
        return entity;
    }
}