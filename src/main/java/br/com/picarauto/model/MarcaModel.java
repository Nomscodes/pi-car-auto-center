package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Marca — tabela "marca".
 * @author Gabriel
 */
@Entity
@Table(name = "marca")
public class MarcaModel extends BaseModel {

    @Column(nullable = false, length = 80)
    private String nome;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
