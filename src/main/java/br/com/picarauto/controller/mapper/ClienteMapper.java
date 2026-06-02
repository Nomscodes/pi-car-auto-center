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
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setEndereco(entity.getEndereco());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setEstado(entity.getEstado());
        dto.setCep(entity.getCep());
        return dto;
    }

    @Override
    public ClienteModel toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        ClienteModel entity = new ClienteModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setEndereco(dto.getEndereco());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setEstado(dto.getEstado());
        entity.setCep(dto.getCep());
        return entity;
    }
}