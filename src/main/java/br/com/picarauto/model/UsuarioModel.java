package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Usuário do sistema — tabela "usuario".
 * @author Caio4breu
 */
@Entity
@Table(name = "usuario")
public class UsuarioModel extends BaseModel {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String perfil;

    @Column(name = "senhaHash", nullable = false)
    private String senhaHash;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
}
