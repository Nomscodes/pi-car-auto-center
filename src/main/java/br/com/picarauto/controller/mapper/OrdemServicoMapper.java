package br.com.picarauto.controller.mapper;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.dto.OrdemServicoDTO;

public class OrdemServicoMapper extends GenericMapper<OrdemServicoModel, OrdemServicoDTO>
        implements IOrdemServicoMapper {

    @Override
    public OrdemServicoDTO toDto(OrdemServicoModel entity) {
        if (entity == null) return null;
        OrdemServicoDTO dto = new OrdemServicoDTO();
        dto.setId(entity.getId());
        dto.setAtivo(entity.isAtivo());
        dto.setNumero(entity.getNumero());
        dto.setDescricaoProblema(entity.getDescricaoProblema());
        dto.setStatusOrdemServico(entity.getStatusOrdemServico());
        dto.setDataAbertura(entity.getDataAbertura());
        dto.setDataConclusao(entity.getDataConclusao());
        dto.setDataEntrada(entity.getDataEntrada());
        dto.setValorMaoDeObra(entity.getValorMaoDeObra());
        dto.setValorPecas(entity.getValorPecas());
        dto.setValorDeslocamento(entity.getValorDeslocamento());
        dto.setValorGincho(entity.getValorGincho());
        dto.setValorOutros(entity.getValorOutros());
        dto.setDesconto(entity.getDesconto());
        dto.setValorTotal(entity.calcularTotal());
        dto.setObservacoes(entity.getObservacoes());
        if (entity.getCliente() != null) dto.setIdCliente(entity.getCliente().getId());
        if (entity.getVeiculo() != null) dto.setIdVeiculo(entity.getVeiculo().getId());
        if (entity.getMecanicoResponsavel() != null) dto.setIdMecanicoResponsavel(entity.getMecanicoResponsavel().getId());
        if (entity.getUsuarioResponsavel() != null) dto.setIdUsuarioResponsavel(entity.getUsuarioResponsavel().getId());
        dto.setServicosExecutados(entity.getServicosExecutados());
        dto.setPecasAplicadas(entity.getPecasAplicadas());
        return dto;
    }

    @Override
    public OrdemServicoModel toEntity(OrdemServicoDTO dto) {
        if (dto == null) return null;
        OrdemServicoModel entity = new OrdemServicoModel();
        entity.setId(dto.getId());
        entity.setAtivo(dto.isAtivo());
        entity.setDescricaoProblema(dto.getDescricaoProblema());
        if (dto.getStatusOrdemServico() != null) entity.setStatusOrdemServico(dto.getStatusOrdemServico());
        entity.setDataAbertura(dto.getDataAbertura());
        entity.setDataConclusao(dto.getDataConclusao());
        entity.setDataEntrada(dto.getDataEntrada());
        entity.setValorMaoDeObra(dto.getValorMaoDeObra());
        entity.setValorPecas(dto.getValorPecas());
        entity.setValorDeslocamento(dto.getValorDeslocamento());
        entity.setValorGincho(dto.getValorGincho());
        entity.setValorOutros(dto.getValorOutros());
        entity.setDesconto(dto.getDesconto());
        entity.setObservacoes(dto.getObservacoes());
        if (dto.getIdCliente() != null) { ClienteModel c = new ClienteModel(); c.setId(dto.getIdCliente()); entity.setCliente(c); }
        if (dto.getIdVeiculo() != null) { VeiculoModel v = new VeiculoModel(); v.setId(dto.getIdVeiculo()); entity.setVeiculo(v); }
        if (dto.getIdMecanicoResponsavel() != null) { MecanicoModel m = new MecanicoModel(); m.setId(dto.getIdMecanicoResponsavel()); entity.setMecanicoResponsavel(m); }
        if (dto.getIdUsuarioResponsavel() != null) { UsuarioModel u = new UsuarioModel(); u.setId(dto.getIdUsuarioResponsavel()); entity.setUsuarioResponsavel(u); }
        if (dto.getServicosExecutados() != null) entity.setServicosExecutados(dto.getServicosExecutados());
        if (dto.getPecasAplicadas() != null) entity.setPecasAplicadas(dto.getPecasAplicadas());
        return entity;
    }
}