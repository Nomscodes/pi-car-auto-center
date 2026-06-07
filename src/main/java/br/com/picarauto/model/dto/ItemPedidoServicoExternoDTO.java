package br.com.picarauto.model.dto;

import java.math.BigDecimal;

/**
 *
 * @author Caio4breu
 */
public class ItemPedidoServicoExternoDTO extends BaseDTO {

    private String descricao;
    private BigDecimal valorCobrado;
    private Integer garantia;
    private String observacoes;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }

    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}