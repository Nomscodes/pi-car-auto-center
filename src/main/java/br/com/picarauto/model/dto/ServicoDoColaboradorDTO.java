package br.com.picarauto.model.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 *
 * @author Gabriel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoDoColaboradorDTO extends BaseDTO {
    private Long idColaborador;
    private Long idServicoInterno;
    private LocalDate dataServico;
}