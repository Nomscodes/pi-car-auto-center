package br.com.picarauto.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 *
 * @author Gabriel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO extends BaseDTO {

    private String nomeFornecedor;
    private String cnpj;
    private String telefone;
    private String email;
}