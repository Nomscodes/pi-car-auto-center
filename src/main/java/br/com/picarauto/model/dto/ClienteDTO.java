package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ClienteDTO extends BaseDTO {
    private String nomeCompleto;
    private String telefone;
    private String email;
    private String endereco;
    private LocalDate dataCadastro;
}