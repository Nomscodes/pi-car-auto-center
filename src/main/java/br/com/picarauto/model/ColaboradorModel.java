package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "colaborador")
@PrimaryKeyJoinColumn(name = "idPessoa")  // ← FK que liga colaborador → pessoa
public class ColaboradorModel extends PessoaModel {

    @Column(name = "dataAdmissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "salario", nullable = false)
    private double salario;

    @Column(name = "idFuncao", nullable = false)
    private Long idFuncao;
}