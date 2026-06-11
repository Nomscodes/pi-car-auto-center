package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "modelo")
public class ModeloModel extends BaseModel {

    @Column(name = "nomeModelo", nullable = false, length = 200)
    private String nomeModelo;

    @Column(name = "anoModelo", nullable = false)
    private Integer anoModelo;

    @Column(name = "idMarca", nullable = false)
    private Long idMarca;
}