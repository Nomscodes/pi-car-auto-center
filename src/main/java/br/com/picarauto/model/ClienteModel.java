package br.com.picarauto.model;

import java.util.Date;

public class ClienteModel extends PessoaModel {

    private Date dataCadastro;

    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }
}