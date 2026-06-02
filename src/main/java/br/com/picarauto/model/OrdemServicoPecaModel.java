package br.com.picarauto.model;

import java.math.BigDecimal;

public class OrdemServicoPecaModel {
    private Integer id;
    private PecaModel peca;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public PecaModel getPeca() { return peca; }
    public void setPeca(PecaModel peca) { this.peca = peca; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getValorTotal() {
        if (quantidade == null || valorUnitario == null) return BigDecimal.ZERO;
        return valorUnitario.multiply(new BigDecimal(quantidade));
    }
}