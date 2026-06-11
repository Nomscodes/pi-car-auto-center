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
@Table(name = "servicoExterno")
public class ServicoExternoModel extends BaseModel {

    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Column(name = "valorCobrado", nullable = false)
    private double valorCobrado;
}