package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
public class ServicoExternoModel extends BaseModel {

    private String descricao;
    private double valorCobrado;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }
}