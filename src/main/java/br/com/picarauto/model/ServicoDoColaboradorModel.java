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
public class ServicoDoColaboradorModel extends BaseModel {

    private ColaboradorModel colaborador;
    private ServicoModel servicoInterno;
    private LocalDate dataServico;

    public ColaboradorModel getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorModel colaborador) {
        this.colaborador = colaborador;
    }

    public ServicoModel getServicoInterno() {
        return servicoInterno;
    }

    public void setServicoInterno(ServicoModel servicoInterno) {
        this.servicoInterno = servicoInterno;
    }

    public LocalDate getDataServico() {
        return dataServico;
    }

    public void setDataServico(LocalDate dataServico) {
        this.dataServico = dataServico;
    }
}
