package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
@Entity
@Table(name = "servicosDoColaborador")
public class ServicoDoColaboradorModel extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idColaborador", nullable = false)
    private ColaboradorModel colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idServicoInterno", nullable = false)
    private ServicoInternoModel servicoInterno;

    @Column(name = "dataServico", nullable = false)
    private LocalDate dataServico;

    public ColaboradorModel getColaborador() { return colaborador; }
    public void setColaborador(ColaboradorModel colaborador) { this.colaborador = colaborador; }

    public ServicoInternoModel getServicoInterno() { return servicoInterno; }
    public void setServicoInterno(ServicoInternoModel servicoInterno) { this.servicoInterno = servicoInterno; }

    public LocalDate getDataServico() { return dataServico; }
    public void setDataServico(LocalDate dataServico) { this.dataServico = dataServico; }

    // Getters de compatibilidade com código que usava Integer diretamente
    public Integer getIdColaborador() { return colaborador != null ? colaborador.getId() : null; }
    public Integer getIdServicoInterno() { return servicoInterno != null ? servicoInterno.getId() : null; }
}
