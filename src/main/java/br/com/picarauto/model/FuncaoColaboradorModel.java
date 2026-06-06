package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Função do Colaborador — tabela "funcaoColaborador".
 * @author Caio4breu
 */
@Entity
@Table(name = "funcaoColaborador")
public class FuncaoColaboradorModel extends BaseModel {

    @Column(nullable = false, length = 80)
    private String funcao;

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }
}
