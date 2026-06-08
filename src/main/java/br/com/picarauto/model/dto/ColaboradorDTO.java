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
public class ColaboradorDTO extends BaseDTO {

    private String nomeCompleto;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataAdmissao;
    private double salario;
    private Long idFuncao;   // Integer → Long (alinhado com BaseModel e IGenericRepository)
}