package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Classe base para todas as entidades do sistema.
 *
 * Padrão de Projeto: Template Method — define estrutura comum (id, dataHoraCriacao, ativo)
 * e o hook onCreate() que subclasses podem usar via @PrePersist.
 *
 * @author Caio4breu
 */
@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataHoraCriacao", updatable = false)
    private Date dataHoraCriacao;

    @Column(nullable = false)
    private boolean ativo;

    @PrePersist
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
