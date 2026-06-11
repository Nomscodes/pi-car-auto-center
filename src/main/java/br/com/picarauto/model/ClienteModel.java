package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "idPessoa")  // ← FK que liga cliente → pessoa
public class ClienteModel extends PessoaModel {

    @Column(name = "dataCadastro", nullable = false)
    private LocalDate dataCadastro;
}