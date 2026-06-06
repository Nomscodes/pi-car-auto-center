package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Fornecedor — tabela "fornecedor".
 * @author Gabriel
 */
@Entity
@Table(name = "fornecedor")
public class FornecedorModel extends BaseModel {

    @Column(nullable = false, length = 150)
    private String nomeFornecedor;

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(length = 20)
    private String telefone;

    @Column(length = 150)
    private String email;

    public String getNomeFornecedor() { return nomeFornecedor; }
    public void setNomeFornecedor(String nomeFornecedor) { this.nomeFornecedor = nomeFornecedor; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
