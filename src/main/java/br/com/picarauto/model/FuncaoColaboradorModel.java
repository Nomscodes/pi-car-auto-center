package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "funcaoColaborador")
public class FuncaoColaboradorModel extends BaseModel {

    @Column(name = "funcao", nullable = false, length = 50)
    private String funcao;
}