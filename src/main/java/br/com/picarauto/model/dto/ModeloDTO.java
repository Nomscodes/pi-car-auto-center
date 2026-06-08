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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDTO extends BaseDTO {

    private String nomeModelo;
    private LocalDate anoModelo;
    private Long idMarca;   // Integer → Long (alinhado com BaseModel e IGenericRepository)
}