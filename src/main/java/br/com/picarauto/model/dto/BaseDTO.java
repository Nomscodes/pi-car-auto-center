package br.com.picarauto.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Caio4breu
 */
@Getter
@Setter
public abstract class BaseDTO {
    private Long id;
    private boolean active;
}
