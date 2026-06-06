package br.com.picarauto.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "itemPedidoPeca")
public class ItemPedidoPecaModel extends BaseModel {

    @Column(nullable = false)
    private int quantidade;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataEntrega")
    private Date dataEntrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigoNacional", referencedColumnName = "codigoNacional", nullable = false)
    private PecaModel peca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFornecedor", nullable = false)
    private FornecedorModel fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOS", nullable = false)
    private OrdemServicoModel ordemServico;

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Date getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(Date dataEntrega) { this.dataEntrega = dataEntrega; }

    public PecaModel getPeca() { return peca; }
    public void setPeca(PecaModel peca) { this.peca = peca; }

    public FornecedorModel getFornecedor() { return fornecedor; }
    public void setFornecedor(FornecedorModel fornecedor) { this.fornecedor = fornecedor; }

    public OrdemServicoModel getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServicoModel ordemServico) { this.ordemServico = ordemServico; }

    // Getters de compatibilidade com código que usava Integer diretamente
    public Integer getCodigoNacional() { return peca != null ? peca.getCodigoNacional() : null; }
    public Integer getIdFornecedor() { return fornecedor != null ? fornecedor.getId() : null; }
    public Integer getIdOS() { return ordemServico != null ? ordemServico.getId() : null; }
}
