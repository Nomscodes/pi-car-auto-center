package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Peça — tabela "peca".
 * @author Caio4breu
 */
@Entity
@Table(name = "peca")
public class PecaModel extends BaseModel {

    // Código nacional: PK de domínio, inserido manualmente (não é auto-incremento)
    @Column(name = "codigoNacional", unique = true, nullable = false)
    private Integer codigoNacional;

    @Column(length = 100)
    private String modelo;

    @Column(length = 80)
    private String marca;

    @Column(name = "anoVeiculo")
    private Integer anoVeiculo;

    @Column(name = "anoModelo")
    private Integer anoModelo;

    @Column(name = "precoUnitario", nullable = false)
    private double precoUnitario;

    @Column(name = "garantia")
    private Integer garantia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFornecedor", nullable = false)
    private FornecedorModel fornecedor;

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

    public FornecedorModel getFornecedor() { return fornecedor; }
    public void setFornecedor(FornecedorModel fornecedor) { this.fornecedor = fornecedor; }

    public Integer getIdFornecedor() { return fornecedor != null ? fornecedor.getId() : null; }
}
