package br.com.picarauto.model;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ordemDeServico")
public class OrdemServicoModel extends BaseModel {

    public enum StatusOrdemServico {
        ORCAMENTO, EXECUCAO, PAGAMENTO, FINALIZADO
    }

    @Column(name = "dataAbertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "dataFechamento")
    private LocalDate dataFechamento;       // null até encerramento

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOrdemServico status = StatusOrdemServico.ORCAMENTO;

    @Column(name = "valorTotal")
    private Double valorTotal;              // nullable — null enquanto em aberto

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "idVeiculo", nullable = false)
    private Long idVeiculo;

    // Campos em memória — usados pela FilaOS para busca por placa e nome do cliente.
    // Não são colunas do banco; devem ser populados antes de enfileirar.
    @Transient
    private String placaVeiculo;

    @Transient
    private String nomeCliente;
}