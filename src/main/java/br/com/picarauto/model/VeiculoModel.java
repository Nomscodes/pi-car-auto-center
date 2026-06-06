package br.com.picarauto.model;

import jakarta.persistence.*;

/**
 * Entidade Veículo — tabela "veiculo".
 *
 * @author Caio4breu
 */
@Entity
@Table(name = "veiculo")
public class VeiculoModel extends BaseModel {

    @Column(length = 8, unique = true, nullable = false)
    private String placa;

    @Column(length = 50)
    private String cor;

    @Column(length = 17, unique = true)
    private String chassi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idModelo", nullable = false)
    private ModeloModel modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = false)
    private ClienteModel cliente;

    // Getters e Setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }

    public ModeloModel getModelo() { return modelo; }
    public void setModelo(ModeloModel modelo) { this.modelo = modelo; }

    public ClienteModel getCliente() { return cliente; }
    public void setCliente(ClienteModel cliente) { this.cliente = cliente; }

    // Atalhos de compatibilidade com código legado que usava idModelo/idCliente por inteiro
    public Integer getIdModelo() { return modelo != null ? modelo.getId() : null; }
    public Integer getIdCliente() { return cliente != null ? cliente.getId() : null; }
}
