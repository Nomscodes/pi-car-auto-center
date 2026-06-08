package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "modelo")
public class ModeloModel extends BaseModel {

    @Column(name = "nomeModelo", nullable = false, length = 200)
    private String nomeModelo;

    @Column(name = "anoModelo", nullable = false)
    private LocalDate anoModelo;

    @Column(name = "idMarca", nullable = false)
    private Long idMarca;                   // Integer → Long
}