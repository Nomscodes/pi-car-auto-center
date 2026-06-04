package br.com.picarauto.model.dto;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoPecaModel;
import br.com.picarauto.model.OrdemServicoServicoModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrdemServicoDTO extends BaseDTO {
    private Long numero;
    private String descricaoProblema;
    private OrdemServicoModel.StatusOrdemServico statusOrdemServico;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private LocalDate dataEntrada;
    private BigDecimal valorMaoDeObra;
    private BigDecimal valorPecas;
    private BigDecimal valorDeslocamento;
    private BigDecimal valorGincho;
    private BigDecimal valorOutros;
    private BigDecimal desconto;
    private BigDecimal valorTotal;
    private String observacoes;
    private Integer idCliente;
    private Integer idVeiculo;
    private Integer idMecanicoResponsavel;
    private Integer idUsuarioResponsavel;
    private List<OrdemServicoServicoModel> servicosExecutados;
    private List<OrdemServicoPecaModel> pecasAplicadas;

    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public OrdemServicoModel.StatusOrdemServico getStatusOrdemServico() { return statusOrdemServico; }
    public void setStatusOrdemServico(OrdemServicoModel.StatusOrdemServico statusOrdemServico) { this.statusOrdemServico = statusOrdemServico; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }
    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }
    public BigDecimal getValorMaoDeObra() { return valorMaoDeObra; }
    public void setValorMaoDeObra(BigDecimal valorMaoDeObra) { this.valorMaoDeObra = valorMaoDeObra; }
    public BigDecimal getValorPecas() { return valorPecas; }
    public void setValorPecas(BigDecimal valorPecas) { this.valorPecas = valorPecas; }
    public BigDecimal getValorDeslocamento() { return valorDeslocamento; }
    public void setValorDeslocamento(BigDecimal valorDeslocamento) { this.valorDeslocamento = valorDeslocamento; }
    public BigDecimal getValorGincho() { return valorGincho; }
    public void setValorGincho(BigDecimal valorGincho) { this.valorGincho = valorGincho; }
    public BigDecimal getValorOutros() { return valorOutros; }
    public void setValorOutros(BigDecimal valorOutros) { this.valorOutros = valorOutros; }
    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public Integer getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Integer idVeiculo) { this.idVeiculo = idVeiculo; }
    public Integer getIdMecanicoResponsavel() { return idMecanicoResponsavel; }
    public void setIdMecanicoResponsavel(Integer idMecanicoResponsavel) { this.idMecanicoResponsavel = idMecanicoResponsavel; }
    public Integer getIdUsuarioResponsavel() { return idUsuarioResponsavel; }
    public void setIdUsuarioResponsavel(Integer idUsuarioResponsavel) { this.idUsuarioResponsavel = idUsuarioResponsavel; }
    public List<OrdemServicoServicoModel> getServicosExecutados() { return servicosExecutados; }
    public void setServicosExecutados(List<OrdemServicoServicoModel> servicosExecutados) { this.servicosExecutados = servicosExecutados; }
    public List<OrdemServicoPecaModel> getPecasAplicadas() { return pecasAplicadas; }
    public void setPecasAplicadas(List<OrdemServicoPecaModel> pecasAplicadas) { this.pecasAplicadas = pecasAplicadas; }
}