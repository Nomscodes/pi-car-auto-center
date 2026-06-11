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
public class ServicoDoColaboradorDTO extends BaseDTO {
    private Long idColaborador;
    private Long idServicoInterno;
    private LocalDate dataServico;
}