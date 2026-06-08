package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)  // ← tabelas separadas com FK
public abstract class PessoaModel extends BaseModel {

    @Column(name = "nomeCompleto", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;
}