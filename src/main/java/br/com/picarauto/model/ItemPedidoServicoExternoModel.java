package br.com.picarauto.model;

import java.math.BigDecimal;

/**
 * Representa a execução de um serviço externo (terceirizado) vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços externos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoExternoFactory}.
 *
 * @author Caio4breu
 */
public class ItemPedidoServicoExternoModel implements IItemServicoOS {

    private Integer id;
    private String descricao;
    private BigDecimal valorCobrado;
    private Integer garantia;
    private String observacoes;

    @Override
    public Integer getId() { return id; }

    @Override
    public BigDecimal getValorCobrado() { return valorCobrado; }

    @Override
    public String getDescricao() { return descricao; }

    public void setId(Integer id) { this.id = id; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}