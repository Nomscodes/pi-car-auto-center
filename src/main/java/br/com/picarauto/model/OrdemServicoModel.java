package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidade Ordem de Serviço — tabela "ordemServico".
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "ordemServico")
public class OrdemServicoModel extends BaseModel {

    public enum StatusOrdemServico {
        ORCAMENTO, EXECUCAO, PAGAMENTO, FINALIZADO
    }

    @Column(name = "dataAbertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "dataFechamento")
    private LocalDate dataFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrdemServico status = StatusOrdemServico.ORCAMENTO;

    @Column(name = "valorTotal", nullable = false)
    private double valorTotal;

    @Column(length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVeiculo", nullable = false)
    private VeiculoModel veiculo;

    // Campos transientes — usados pela FilaOS em memória, não persistidos
    @Transient
    private String placaVeiculo;

    @Transient
    private String nomeCliente;

    // Getters e Setters
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }

    public StatusOrdemServico getStatus() { return status; }
    public void setStatus(StatusOrdemServico status) { this.status = status; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public VeiculoModel getVeiculo() { return veiculo; }
    public void setVeiculo(VeiculoModel veiculo) { this.veiculo = veiculo; }

    public String getPlacaVeiculo() { return placaVeiculo; }
    public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    // Atalho de compatibilidade
    public Integer getIdVeiculo() { return veiculo != null ? veiculo.getId() : null; }
}
