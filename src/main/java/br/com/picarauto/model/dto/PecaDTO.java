package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
public class PecaDTO extends BaseDTO {

    private Integer codigoNacional;
    private String modelo;
    private String marca;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private double precoUnitario;
    private Integer garantia;
    private Integer idFornecedor;

    public Integer getCodigoNacional() { return codigoNacional; }
    public void setCodigoNacional(Integer codigoNacional) { this.codigoNacional = codigoNacional; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public Integer getAnoVeiculo() { return anoVeiculo; }
    public void setAnoVeiculo(Integer anoVeiculo) { this.anoVeiculo = anoVeiculo; }
    public Integer getAnoModelo() { return anoModelo; }
    public void setAnoModelo(Integer anoModelo) { this.anoModelo = anoModelo; }
    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }
    public Integer getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(Integer idFornecedor) { this.idFornecedor = idFornecedor; }
}