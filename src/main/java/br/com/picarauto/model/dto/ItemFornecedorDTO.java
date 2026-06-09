package br.com.picarauto.model.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ItemFornecedorDTO extends BaseDTO {
    private Long idFornecedor;
    private Long idItemPedidoServicoExterno;
    private LocalDate dataExecucao;
}