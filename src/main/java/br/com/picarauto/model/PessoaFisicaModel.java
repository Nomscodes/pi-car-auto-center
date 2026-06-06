package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entidade Pessoa Física — tabela "pessoaFisica".
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "pessoaFisica")
public class PessoaFisicaModel extends ClienteModel {

    @Column(length = 14, unique = true)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataNascimento")
    private Date dataNascimento;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
}
