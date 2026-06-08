package br.com.picarauto.model;

/**
 *
 * @author Gabriel
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fornecedor")
public class FornecedorModel extends BaseModel {

    @Column(name = "nomeFornecedor", nullable = false, length = 200)
    private String nomeFornecedor;

    @Column(name = "cnpj", unique = true, length = 14)
    private String cnpj;                // nullable — fornecedor pode não ter CNPJ

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;            // obrigatório mas não único

    @Column(name = "email", nullable = false, length = 150)
    private String email;
}
