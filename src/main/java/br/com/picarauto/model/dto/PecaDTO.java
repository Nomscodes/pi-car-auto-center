package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PecaDTO extends BaseDTO {
    private Integer codigoNacional;
    private String modelo;
    private String marca;
    private Integer anoVeiculo;
    private Integer anoModelo;
    private double precoUnitario;
    private Integer garantia;
    private Long idFornecedor;
}