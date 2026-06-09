package br.com.picarauto.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoVeiculoDTO extends BaseDTO {
    private Long idPessoa;
    private Long idVeiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}