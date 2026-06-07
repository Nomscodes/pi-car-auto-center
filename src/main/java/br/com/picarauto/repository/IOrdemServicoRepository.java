package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoModel.StatusOrdemServico;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdemServicoRepository extends IGenericRepository<OrdemServicoModel> {
    List<OrdemServicoModel> findAllByIdVeiculo(Long idVeiculo);
    List<OrdemServicoModel> findAllByStatus(StatusOrdemServico status);
}