package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Representa a execução de um serviço externo (terceirizado) vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços externos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoExternoFactory}.
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "itemPedidoServicoExterno")
public class ItemPedidoServicoExternoModel extends BaseModel implements IItemServicoOS {

    @Column(name = "valorItem", nullable = false)
    private Double valorItem;

    @Column(nullable = false)
    private Integer garantia;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idServicoExterno", nullable = false)
    private ServicoExternoModel servicoExterno;

    // Campo em memória — não é coluna do banco.
    // Populado pelo service antes de usar na view ou no decorator.
    @Transient
    private Integer idOS;

    @Override
    public Integer getId() { return super.getId(); }

    @Override
    public String getDescricao() { return observacoes; }

    public Double getValorItem() { return valorItem; }
    public void setValorItem(Double valorItem) { this.valorItem = valorItem; }

    public Integer getGarantia() { return garantia; }
    public void setGarantia(Integer garantia) { this.garantia = garantia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public ServicoExternoModel getServicoExterno() { return servicoExterno; }
    public void setServicoExterno(ServicoExternoModel servicoExterno) { this.servicoExterno = servicoExterno; }

    // Getter de compatibilidade
    public Integer getIdServicoExterno() { return servicoExterno != null ? servicoExterno.getId() : null; }

    public Integer getIdOS() { return idOS; }
    public void setIdOS(Integer idOS) { this.idOS = idOS; }
}
