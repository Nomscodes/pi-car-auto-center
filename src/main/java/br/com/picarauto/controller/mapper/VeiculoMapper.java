package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.dto.VeiculoDTO;

public class VeiculoMapper extends GenericMapper<VeiculoModel, VeiculoDTO> implements IVeiculoMapper {

    @Override
    public VeiculoDTO toDto(VeiculoModel entity) {
        if (entity == null) return null;
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setPlaca(entity.getPlaca());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setAnoFabricacao(entity.getAnoFabricacao());
        dto.setCor(entity.getCor());
        dto.setQuilometragem(entity.getQuilometragem());
        if (entity.getCliente() != null)
            dto.setIdCliente(entity.getCliente().getId());
        return dto;
    }

    @Override
    public VeiculoModel toEntity(VeiculoDTO dto) {
        if (dto == null) return null;
        VeiculoModel entity = new VeiculoModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setPlaca(dto.getPlaca());
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setAnoFabricacao(dto.getAnoFabricacao());
        entity.setCor(dto.getCor());
        entity.setQuilometragem(dto.getQuilometragem());
        if (dto.getIdCliente() != null) {
            ClienteModel cliente = new ClienteModel();
            cliente.setId(dto.getIdCliente());
            entity.setCliente(cliente);
        }
        return entity;
    }
}