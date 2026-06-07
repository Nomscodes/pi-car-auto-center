/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.model;

import br.com.picarauto.model.base.BaseModel;
import java.time.LocalDate;

/**
 *
 * @author Gabriel
 */
public class ServicoDoColaboradorModel extends BaseModel {

    private Integer idColaborador;    
    private Integer idServicoInterno; 
    private LocalDate dataServico;

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Integer getIdServicoInterno() {
        return idServicoInterno;
    }

    public void setIdServicoInterno(Integer idServicoInterno) {
        this.idServicoInterno = idServicoInterno;
    }

    public LocalDate getDataServico() {
        return dataServico;
    }

    public void setDataServico(LocalDate dataServico) {
        this.dataServico = dataServico;
    }
}
