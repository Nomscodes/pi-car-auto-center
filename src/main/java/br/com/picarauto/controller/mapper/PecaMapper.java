package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.dto.PecaDTO;

public class PecaMapper extends GenericMapper<PecaModel, PecaDTO> implements IPecaMapper {

    @Override
    public PecaDTO toDto(PecaModel entity) {
        if (entity == null) return null;
        PecaDTO dto = new PecaDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNome(entity.getNome());
        dto.setQuantidade(entity.getQuantidade());
        dto.setValorUnitario(entity.getValorUnitario());
        return dto;
    }

    @Override
    public PecaModel toEntity(PecaDTO dto) {
        if (dto == null) return null;
        PecaModel entity = new PecaModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setNome(dto.getNome());
        entity.setQuantidade(dto.getQuantidade());
        entity.setValorUnitario(dto.getValorUnitario());
        return entity;
    }
}