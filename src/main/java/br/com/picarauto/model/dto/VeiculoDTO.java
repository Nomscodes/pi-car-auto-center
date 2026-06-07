package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoDTO extends BaseDTO {
    private String placa;
    private String cor;
    private String chassi;
    private Long idModelo;
    private Long idCliente;
}