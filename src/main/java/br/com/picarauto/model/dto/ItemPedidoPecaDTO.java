package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ItemPedidoPecaDTO extends BaseDTO {
    private int quantidade;
    private LocalDate dataEntrega;
    private Long codigoNacional;
    private Long idFornecedor;
    private Long idOS;
}