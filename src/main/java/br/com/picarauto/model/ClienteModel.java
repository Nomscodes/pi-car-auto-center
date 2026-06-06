package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public class ClienteModel extends PessoaModel {

    @Temporal(TemporalType.DATE)
    @Column(name = "dataCadastro")
    private Date dataCadastro;

    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }
}