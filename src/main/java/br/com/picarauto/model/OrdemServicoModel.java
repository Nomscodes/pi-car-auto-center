package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import java.time.LocalDate;

public class OrdemServicoModel extends BaseModel {

    public enum StatusOrdemServico {
        ORCAMENTO, EXECUCAO, PAGAMENTO, FINALIZADO
    }

    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
    private StatusOrdemServico status = StatusOrdemServico.ORCAMENTO;
    private double valorTotal;
    private String observacoes;
    private Integer idVeiculo;

    // Campos em memória — usados pela FilaOS para busca por placa e nome do cliente.
    // Não são colunas do banco; devem ser populados antes de enfileirar.
    private String placaVeiculo;
    private String nomeCliente;

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
    public Integer getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Integer idVeiculo) { this.idVeiculo = idVeiculo; }
    public String getPlacaVeiculo() { return placaVeiculo; }
    public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
}