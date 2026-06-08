/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "itemFornecedor")
public class ItemFornecedorModel extends BaseModel {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFornecedor", nullable = false)
    private FornecedorModel fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idItemPedidoServicoExterno", nullable = false)
    private ItemPedidoServicoExternoModel itemPedidoServicoExterno;

    @Column(name = "dataExecucao", nullable = false)
    private LocalDate dataExecucao;

    public FornecedorModel getFornecedor() { return fornecedor; }
    public void setFornecedor(FornecedorModel fornecedor) { this.fornecedor = fornecedor; }

    public ItemPedidoServicoExternoModel getItemPedidoServicoExterno() { return itemPedidoServicoExterno; }
    public void setItemPedidoServicoExterno(ItemPedidoServicoExternoModel itemPedidoServicoExterno) { this.itemPedidoServicoExterno = itemPedidoServicoExterno; }

    public LocalDate getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDate dataExecucao) { this.dataExecucao = dataExecucao; }

    // Getters de compatibilidade
    public Integer getIdFornecedor() { return fornecedor != null ? fornecedor.getId() : null; }
    public Integer getIdItemPedidoServicoExterno() { return itemPedidoServicoExterno != null ? itemPedidoServicoExterno.getId() : null; }
}
