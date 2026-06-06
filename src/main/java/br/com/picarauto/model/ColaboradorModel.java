package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidade Colaborador — tabela "colaborador".
 * @author Gabriel
 */
@Entity
@Table(name = "colaborador")
public class ColaboradorModel extends PessoaModel {

    @Column(name = "dataAdmissao")
    private LocalDate dataAdmissao;

    @Column(nullable = false)
    private double salario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFuncao", nullable = false)
    private FuncaoColaboradorModel funcao;

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public FuncaoColaboradorModel getFuncao() { return funcao; }
    public void setFuncao(FuncaoColaboradorModel funcao) { this.funcao = funcao; }

    public Integer getIdFuncao() { return funcao != null ? funcao.getId() : null; }
}
