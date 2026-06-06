/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.model;

import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
public class ItemFornecedorModel extends BaseModel {
    
    private Integer idFornecedor;
    private Integer idItemPedidoServicoExterno;
    private LocalDate dataExecucao;

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Integer getIdItemPedidoServicoExterno() {
        return idItemPedidoServicoExterno;
    }

    public void setIdItemPedidoServicoExterno(Integer idItemPedidoServicoExterno) {
        this.idItemPedidoServicoExterno = idItemPedidoServicoExterno;
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }
}
