package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidade Histórico de Proprietários do Veículo — tabela "historicoVeiculo".
 * @author Gabriel
 */
@Entity
@Table(name = "historicoVeiculo")
public class HistoricoVeiculoModel extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPessoa", nullable = false)
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVeiculo", nullable = false)
    private VeiculoModel veiculo;

    @Column(name = "dataInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "dataFim")
    private LocalDate dataFim;

    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    public VeiculoModel getVeiculo() { return veiculo; }
    public void setVeiculo(VeiculoModel veiculo) { this.veiculo = veiculo; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public Integer getIdPessoa() { return cliente != null ? cliente.getId() : null; }
    public Integer getIdVeiculo() { return veiculo != null ? veiculo.getId() : null; }
}
