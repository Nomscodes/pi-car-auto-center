package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "itemPedidoPeca")
public class ItemPedidoPecaModel extends BaseModel {

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "dataEntrega")
    private LocalDate dataEntrega;       // NULL até entrega confirmada

    @Column(name = "codigoNacional", nullable = false)
    private Long codigoNacional;

    @Column(name = "idFornecedor", nullable = false)
    private Long idFornecedor;

    @Column(name = "idOS", nullable = false)
    private Long idOS;
}