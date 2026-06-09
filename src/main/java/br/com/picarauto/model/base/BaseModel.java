package br.com.picarauto.model.base;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Caio4breu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "data_hora_criacao", updatable = false)
    private LocalDateTime dataHoraCriacao;

    @Column(name = "ativo")
    private boolean ativo;

    @PrePersist
    public void onCreate() {
        if (this.dataHoraCriacao == null) {
            this.dataHoraCriacao = LocalDateTime.now();
        }
        this.ativo = true;
    }
}