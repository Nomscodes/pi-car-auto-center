package br.com.picarauto.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidade Modelo de Veículo — tabela "modelo".
 * @author Gabriel
 */
@Entity
@Table(name = "modelo")
public class ModeloModel extends BaseModel {

    @Column(nullable = false, length = 100)
    private String nomeModelo;

    @Column(name = "anoModelo")
    private LocalDate anoModelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idMarca", nullable = false)
    private MarcaModel marca;

    public String getNomeModelo() { return nomeModelo; }
    public void setNomeModelo(String nomeModelo) { this.nomeModelo = nomeModelo; }

    public LocalDate getAnoModelo() { return anoModelo; }
    public void setAnoModelo(LocalDate anoModelo) { this.anoModelo = anoModelo; }

    public MarcaModel getMarca() { return marca; }
    public void setMarca(MarcaModel marca) { this.marca = marca; }

    public Integer getIdMarca() { return marca != null ? marca.getId() : null; }
}
