package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Representa uma pessoa no sistema (abstrata).
 * Mapeada com herança JOINED: cada subclasse tem sua própria tabela,
 * ligada a esta via chave estrangeira — reflete o modelo relacional do schema.
 *
 * @author Caio4breu
 */
@MappedSuperclass
public abstract class PessoaModel extends BaseModel {

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(length = 20)
    private String telefone;

    @Column(length = 150)
    private String email;

    @Column(length = 255)
    private String endereco;

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
