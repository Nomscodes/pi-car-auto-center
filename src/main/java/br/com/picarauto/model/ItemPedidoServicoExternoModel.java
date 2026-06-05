package br.com.picarauto.model;

/**
 * Representa a execução de um serviço externo (terceirizado) vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços externos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoExternoFactory}.
 *
 * @author Caio4breu
 */
public class ItemPedidoServicoExternoModel extends BaseModel implements IItemServicoOS {

    private Double valorItem;
    private Integer garantia;
    private String observacoes;
    private Integer idServicoExterno;

    @Override
    public Integer getId() { return super.getId(); }

    @Override
    public String getDescricao() { return observacoes; }

    public Double getValorItem() { return valorItem; }
    public void setValorItem(Double valorItem) { this.valorItem = valorItem; }
    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Integer getIdServicoExterno() { return idServicoExterno; }
    public void setIdServicoExterno(Integer idServicoExterno) { this.idServicoExterno = idServicoExterno; }
}