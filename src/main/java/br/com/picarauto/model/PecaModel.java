package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "peca")
public class PecaModel extends BaseModel {

    @Column(name = "codigoNacional", nullable = false, unique = true)
    private Integer codigoNacional;     // PK de domínio — manual, não é SERIAL

    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @Column(name = "anoVeiculo", nullable = false)
    private Integer anoVeiculo;

    @Column(name = "anoModelo", nullable = false)
    private Integer anoModelo;

    @Column(name = "precoUnitario", nullable = false)
    private double precoUnitario;

    @Column(name = "garantia", nullable = false)
    private Integer garantia;

    @Column(name = "idFornecedor", nullable = false)
    private Long idFornecedor;
}