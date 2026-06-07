package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemServicoInternoDTO extends BaseDTO {
    private double valorItem;
    private int garantia;
    private String observacoes;
    private Long idOS;
}