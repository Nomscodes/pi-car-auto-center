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
@Table(name = "cliente")
public class ClienteModel extends PessoaModel {

    @Column(name = "dataCadastro", nullable = false)
    private LocalDate dataCadastro;
}