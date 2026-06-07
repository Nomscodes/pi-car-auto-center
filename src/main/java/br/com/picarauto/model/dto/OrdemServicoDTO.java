package br.com.picarauto.model.dto;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class OrdemServicoDTO extends BaseDTO {
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;       // null até encerramento
    private StatusOrdemServico status;
    private Double valorTotal;              // nullable — null enquanto em aberto
    private String observacoes;
    private Long idVeiculo;

    // Campos em memória — populados antes de enfileirar na FilaOS
    private String placaVeiculo;
    private String nomeCliente;
}