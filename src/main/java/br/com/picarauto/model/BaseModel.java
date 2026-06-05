package br.com.picarauto.model;

import java.util.Date;

/**
 * 
 * @author Caio4breu
 */
public abstract class BaseModel {
    private Integer id;
    private Date dataHoraCriacao;
    private boolean ativo;

    public void onCreate() {
        if (this.dataHoraCriacao == null) {
            this.dataHoraCriacao = new Date();
        }
        this.ativo = true;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Date getDataHoraCriacao() { return dataHoraCriacao; }
    public void setDataHoraCriacao(Date dataHoraCriacao) { this.dataHoraCriacao = dataHoraCriacao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}