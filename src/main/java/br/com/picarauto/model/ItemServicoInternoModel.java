package br.com.picarauto.model;

/**
 * Representa a execução de um serviço interno vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços internos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoInternoFactory}.
 *
 * @author Caio4breu
 */
public class ItemServicoInternoModel extends BaseModel implements IItemServicoOS {

    private double valorItem;
    private int garantia;
    private String observacoes;
    private Integer idOS;

    @Override
    public Integer getId() { return super.getId(); }

    @Override
    public String getDescricao() { return observacoes; }

    public double getValorItem() { return valorItem; }
    public void setValorItem(double valorItem) { this.valorItem = valorItem; }
    public int getGarantia() { return garantia; }
    public void setGarantia(int garantia) { this.garantia = garantia; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Integer getIdOS() { return idOS; }
    public void setIdOS(Integer idOS) { this.idOS = idOS; }
}