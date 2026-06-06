package br.com.picarauto.repository;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório Spring Data para OrdemServicoModel.
 */
@Repository
public interface IOrdemServicoRepository extends IGenericRepository<OrdemServicoModel> {
    // Busca OS por veículo — Spring Data navega pelo relacionamento @ManyToOne
    List<OrdemServicoModel> findAllByVeiculoId(Integer idVeiculo);
    // Busca OS por status usando o enum diretamente
    List<OrdemServicoModel> findAllByStatus(StatusOrdemServico status);
    List<OrdemServicoModel> findAllByAtivoTrueOrderByDataAberturaDesc();
}
