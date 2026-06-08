package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoExternoDTO extends BaseDTO {
    private String descricao;
    private double valorCobrado;
}