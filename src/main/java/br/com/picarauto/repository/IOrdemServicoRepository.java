package br.com.picarauto.repository;

import br.com.picarauto.model.OrdemServicoModel;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public interface IOrdemServicoRepository extends IGenericRepository<OrdemServicoModel> {
    List<OrdemServicoModel> findAllByIdVeiculo(Integer idVeiculo);
    List<OrdemServicoModel> findAllByStatus(String status);
}