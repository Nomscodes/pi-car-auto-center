package br.com.picarauto.model;

import java.util.Date;

public class PessoaFisicaModel extends ClienteModel {

    private String cpf;
    private String rg;
    private Date dataNascimento;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
}