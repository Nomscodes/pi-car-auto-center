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
public class ModeloModel extends BaseModel{

    private String nomeModelo;
    private LocalDate anoModelo;
    private Integer idMarca;

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public LocalDate getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(LocalDate anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }
}
