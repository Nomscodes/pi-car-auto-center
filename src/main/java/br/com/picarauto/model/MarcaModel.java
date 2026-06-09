package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "marca")
public class MarcaModel extends BaseModel {

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
}