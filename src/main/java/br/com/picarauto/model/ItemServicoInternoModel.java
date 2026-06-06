package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Representa a execução de um serviço interno vinculado a uma OS.
 *
 * Implementa {@link IItemServicoOS} para que a factory de serviços internos
 * possa criá-lo polimorficamente via {@link br.com.picarauto.factory.ServicoInternoFactory}.
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "itemServicoInterno")
public class ItemServicoInternoModel extends BaseModel implements IItemServicoOS {

    @Column(name = "valorItem", nullable = false)
    private double valorItem;

    @Column(nullable = false)
    private int garantia;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOS", nullable = false)
    private OrdemServicoModel ordemServico;

    @Override
    public Integer getId() { return super.getId(); }

    @Override
    public String getDescricao() { return observacoes; }

    public double getValorItem() { return valorItem; }
    public void setValorItem(double valorItem) { this.valorItem = valorItem; }

    public int getGarantia() { return garantia; }
    public void setGarantia(int garantia) { this.garantia = garantia; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public OrdemServicoModel getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServicoModel ordemServico) { this.ordemServico = ordemServico; }

    // Getter de compatibilidade com código que usava idOS como Integer
    public Integer getIdOS() { return ordemServico != null ? ordemServico.getId() : null; }
}
