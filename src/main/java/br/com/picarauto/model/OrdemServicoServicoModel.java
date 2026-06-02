package br.com.picarauto.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrdemServicoServicoModel {
    private Integer id;
    private ServicoModel servico;
    private MecanicoModel mecanicoExecutor;
    private BigDecimal horasExecutadas;
    private BigDecimal valorCobrado;
    private Date horarioInicio;
    private Date horarioFim;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public ServicoModel getServico() { return servico; }
    public void setServico(ServicoModel servico) { this.servico = servico; }
    public MecanicoModel getMecanicoExecutor() { return mecanicoExecutor; }
    public void setMecanicoExecutor(MecanicoModel mecanicoExecutor) { this.mecanicoExecutor = mecanicoExecutor; }
    public BigDecimal getHorasExecutadas() { return horasExecutadas; }
    public void setHorasExecutadas(BigDecimal horasExecutadas) { this.horasExecutadas = horasExecutadas; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(BigDecimal valorCobrado) { this.valorCobrado = valorCobrado; }
    public Date getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(Date horarioInicio) { this.horarioInicio = horarioInicio; }
    public Date getHorarioFim() { return horarioFim; }
    public void setHorarioFim(Date horarioFim) { this.horarioFim = horarioFim; }
}