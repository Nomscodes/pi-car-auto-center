package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entidade Pessoa Jurídica — tabela "pessoaJuridica".
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "pessoaJuridica")
public class PessoaJuridicaModel extends ClienteModel {

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(length = 150)
    private String razaoSocial;

    @Column(length = 150)
    private String nomeFantasia;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataAbertura")
    private Date dataAbertura;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public Date getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(Date dataAbertura) { this.dataAbertura = dataAbertura; }
}
