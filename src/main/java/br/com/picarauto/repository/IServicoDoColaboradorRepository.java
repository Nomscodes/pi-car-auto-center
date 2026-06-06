package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoDoColaboradorModel;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Gabriel
 */
@Repository
public interface IServicoDoColaboradorRepository extends IGenericRepository<ServicoDoColaboradorModel> {
    // Busca todos os serviços executados por um colaborador
    List<ServicoDoColaboradorModel> findAllByColaboradorId(Integer idColaborador);

    // Busca todos os colaboradores que executaram um serviço interno
    List<ServicoDoColaboradorModel> findAllByServicoInternoId(Integer idServicoInterno);

    // Verifica se um colaborador já tem vínculo com um serviço interno específico
    boolean existsByColaboradorIdAndServicoInternoId(Integer idColaborador, Integer idServicoInterno);
}
