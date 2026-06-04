package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
public class ItemServicoInternoModel extends BaseModel {
    private double valorItem;
    private int garantia;
    private String observacoes;
    private Integer idOS;
 
    public double getValorItem() { return valorItem; }
    public void setValorItem(double valorItem) { this.valorItem = valorItem; }
 
    public int getGarantia() { return garantia; }
    public void setGarantia(int garantia) { this.garantia = garantia; }
 
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
 
    public Integer getIdOS() { return idOS; }
    public void setIdOS(Integer idOS) { this.idOS = idOS; }    
}