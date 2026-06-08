package br.com.picarauto.model.base;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @author Caio4breu
 */
@Data
@MappedSuperclass  // diz ao JPA que esta classe é base, mas não tem tabela própria
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // equivale ao SERIAL do PostgreSQL
    @Column(name = "id", insertable = false, updatable = false)
    private Long id;

    @Column(name = "data_hora_criacao", updatable = false)
    private LocalDateTime dataHoraCriacao;

    @Column(name = "ativo")
    private boolean ativo;

    @PrePersist  // substitui o onCreate() manual — JPA chama isso automaticamente antes de salvar
    public void onCreate() {
        if (this.dataHoraCriacao == null) {
            this.dataHoraCriacao = LocalDateTime.now();
        }
        this.ativo = true;
    }
}
