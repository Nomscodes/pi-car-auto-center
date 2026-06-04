package br.com.picarauto.model;

import java.util.Date;

/**
 * 
 * @author Caio4breu
 */
public class ItemPedidoPecaModel extends BaseModel {

    private int quantidade;
    private Date dataEntrega;
    private Integer codigoNacional;
    private Integer idFornecedor;
    private Integer idOS;

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Date getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(Date dataEntrega) { this.dataEntrega = dataEntrega; }

    public Integer getCodigoNacional() { return codigoNacional; }
    public void setCodigoNacional(Integer codigoNacional) { this.codigoNacional = codigoNacional; }

    public Integer getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Integer idFornecedor) { this.idFornecedor = idFornecedor; }

    public Integer getIdOS() { return idOS; }
    public void setIdOS(Integer idOS) { this.idOS = idOS; }
}