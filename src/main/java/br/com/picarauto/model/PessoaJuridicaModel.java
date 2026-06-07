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
@Table(name = "pessoaJuridica")
public class PessoaJuridicaModel extends ClienteModel {

    @Column(name = "cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(name = "razaoSocial", nullable = false, length = 150)
    private String razaoSocial;

    @Column(name = "nomeFantasia", length = 150)
    private String nomeFantasia;            // nullable — campo opcional no banco

    @Column(name = "dataAbertura", nullable = false)
    private LocalDate dataAbertura;
}