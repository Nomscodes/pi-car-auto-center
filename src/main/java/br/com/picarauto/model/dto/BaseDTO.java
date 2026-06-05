package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
public abstract class BaseDTO {

    private Integer id;
    private boolean ativo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
