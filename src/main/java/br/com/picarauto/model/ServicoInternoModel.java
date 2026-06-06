package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Serviço Interno — tabela "servicoInterno".
 * @author Caio4breu
 */
@Entity
@Table(name = "servicoInterno")
public class ServicoInternoModel extends BaseModel {

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "valorCobrado", nullable = false)
    private double valorCobrado;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }
}
