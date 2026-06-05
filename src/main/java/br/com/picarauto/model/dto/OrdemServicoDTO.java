package br.com.picarauto.model.dto;

import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import java.time.LocalDate;

/**
 *
 * @author Caio4breu
 */
public class OrdemServicoDTO extends BaseDTO {

    private LocalDate dataAbertura;
    private LocalDate dataFechamento;   // null até encerramento
    private StatusOrdemServico status;
    private Double valorTotal;          // Double (nullable) — null enquanto em aberto
    private String observacoes;
    private Integer idVeiculo;

    // Campos em memória — populados antes de enfileirar na FilaOS
    private String placaVeiculo;
    private String nomeCliente;

    // ── getters / setters ──────────────────────────────────────────────────

    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDate dataFechamento) { this.dataFechamento = dataFechamento; }

    public StatusOrdemServico getStatus() { return status; }
    public void setStatus(StatusOrdemServico status) { this.status = status; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Integer getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Integer idVeiculo) { this.idVeiculo = idVeiculo; }

    public String getPlacaVeiculo() { return placaVeiculo; }
    public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
}