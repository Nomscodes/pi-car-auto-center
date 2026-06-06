package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entidade Cliente — tabela "cliente".
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "cliente")
public class ClienteModel extends PessoaModel {

    @Temporal(TemporalType.DATE)
    @Column(name = "dataCadastro")
    private Date dataCadastro;

    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }
}
