package br.com.picarauto.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class OrdemServicoModel extends BaseModel {
    public enum StatusOrdemServico {
        ORCAMENTO, EXECUCAO, PAGAMENTO, FINALIZADO
    }

    private Long numero;
    private String descricaoProblema;
    private StatusOrdemServico statusOrdemServico = StatusOrdemServico.ORCAMENTO;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private LocalDate dataEntrada;
    private BigDecimal valorMaoDeObra;
    private BigDecimal valorPecas;
    private BigDecimal valorDeslocamento;
    private BigDecimal valorGincho;
    private BigDecimal valorOutros;
    private BigDecimal desconto;
    private String observacoes;
    private ClienteModel cliente;
    private VeiculoModel veiculo;
    private MecanicoModel mecanicoResponsavel;
    private UsuarioModel usuarioResponsavel;
    private List<OrdemServicoServicoModel> servicosExecutados = new ArrayList<>();
    private List<OrdemServicoPecaModel> pecasAplicadas = new ArrayList<>();

    public Long getNumero() { return numero; }
    public void setNumero(Long numero) { this.numero = numero; }
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public StatusOrdemServico getStatusOrdemServico() { return statusOrdemServico; }
    public void setStatusOrdemServico(StatusOrdemServico statusOrdemServico) { this.statusOrdemServico = statusOrdemServico; }
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
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }
    public VeiculoModel getVeiculo() { return veiculo; }
    public void setVeiculo(VeiculoModel veiculo) { this.veiculo = veiculo; }
    public MecanicoModel getMecanicoResponsavel() { return mecanicoResponsavel; }
    public void setMecanicoResponsavel(MecanicoModel mecanicoResponsavel) { this.mecanicoResponsavel = mecanicoResponsavel; }
    public UsuarioModel getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(UsuarioModel usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }
    public List<OrdemServicoServicoModel> getServicosExecutados() { return servicosExecutados; }
    public void setServicosExecutados(List<OrdemServicoServicoModel> servicosExecutados) { this.servicosExecutados = servicosExecutados; }
    public List<OrdemServicoPecaModel> getPecasAplicadas() { return pecasAplicadas; }
    public void setPecasAplicadas(List<OrdemServicoPecaModel> pecasAplicadas) { this.pecasAplicadas = pecasAplicadas; }

    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (valorMaoDeObra != null) total = total.add(valorMaoDeObra);
        if (valorPecas != null) total = total.add(valorPecas);
        if (valorDeslocamento != null) total = total.add(valorDeslocamento);
        if (valorGincho != null) total = total.add(valorGincho);
        if (valorOutros != null) total = total.add(valorOutros);
        if (desconto != null) total = total.subtract(desconto);
        return total;
    }
}