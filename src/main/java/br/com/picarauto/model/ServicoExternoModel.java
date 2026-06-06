package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Serviço Externo (terceirizado) — tabela "servicoExterno".
 * @author Caio4breu
 */
@Entity
@Table(name = "servicoExterno")
public class ServicoExternoModel extends BaseModel {

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "valorCobrado", nullable = false)
    private double valorCobrado;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }
}
