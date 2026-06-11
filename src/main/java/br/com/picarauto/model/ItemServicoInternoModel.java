package br.com.picarauto.model;

/**
 * Representa a execução de um serviço interno vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços internos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoInternoFactory}.
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "itemServicoInterno")
public class ItemServicoInternoModel extends BaseModel implements IItemServicoOS {

    @Column(name = "valorItem", nullable = false)
    private double valorItem;

    @Column(name = "garantia", nullable = false)
    private int garantia;

    @Column(name = "observacoes", length = 500, nullable = false)
    private String observacoes;

    @Column(name = "idOS", nullable = false)
    private Long idOS;

    @Override
    public Long getId() { return super.getId(); }

    @Override
    public String getDescricao() { return observacoes; }
}