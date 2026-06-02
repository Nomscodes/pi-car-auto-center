package br.com.picarauto.model;

public class MecanicoModel extends BaseModel {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String especialidade;
    private String crea;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getCrea() { return crea; }
    public void setCrea(String crea) { this.crea = crea; }
}