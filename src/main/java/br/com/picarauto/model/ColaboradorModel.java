package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "colaborador")
@PrimaryKeyJoinColumn(name = "idPessoa")
public class ColaboradorModel extends PessoaModel {

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "dataAdmissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "salario", nullable = false)
    private double salario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idFuncao")
    private FuncaoColaboradorModel funcao;
}