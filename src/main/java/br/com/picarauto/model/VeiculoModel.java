package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "veiculo")
public class VeiculoModel extends BaseModel {

    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @Column(name = "cor", nullable = false, length = 50)
    private String cor;

    @Column(name = "chassi", nullable = false, unique = true, length = 17)
    private String chassi;

    @Column(name = "idModelo", nullable = false)
    private Long idModelo;

    @Column(name = "idCliente", nullable = false)
    private Long idCliente;
}