package br.com.picarauto.model;

/**
 * Representa a execução de um serviço externo (terceirizado) vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços externos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoExternoFactory}.
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "itemPedidoServicoExterno")
public class ItemPedidoServicoExternoModel extends BaseModel implements IItemServicoOS {

    @Column(name = "valorItem", nullable = false)
    private Double valorItem;

    @Column(name = "garantia", nullable = false)
    private Integer garantia;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "idServicoExterno", nullable = false)
    private Long idServicoExterno;

    @Transient
    private Long idOS;

    @Override
    public String getDescricao() { return observacoes; }
}