package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pessoaFisica")
@PrimaryKeyJoinColumn(name = "idCliente")  // ← FK que liga pessoaFisica → cliente
public class PessoaFisicaModel extends ClienteModel {

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "rg", nullable = false, unique = true, length = 20)
    private String rg;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate dataNascimento;
}