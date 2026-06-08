package br.com.picarauto.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoVeiculoDTO extends BaseDTO {
    private Long idPessoa;
    private Long idVeiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}