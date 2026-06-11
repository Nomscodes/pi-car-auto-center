package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.util.OrdenadorOS;
import br.com.picarauto.validation.IOrdemServicoValidation;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Caio4breu
 */
public interface IOrdemServicoService extends IGenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation> {

    /** Retorna as OS da fila ordenadas por ID em ordem crescente. */
    List<OrdemServicoModel> listarOrdenadoPorIdAsc();

    /** Retorna as OS da fila ordenadas por ID em ordem decrescente. */
    List<OrdemServicoModel> listarOrdenadoPorIdDesc();

    /** Retorna as OS da fila ordenadas por nome do cliente de A a Z. */
    List<OrdemServicoModel> listarOrdenadoPorNomeClienteAsc();

    /** Retorna as OS da fila ordenadas por nome do cliente de Z a A. */
    List<OrdemServicoModel> listarOrdenadoPorNomeClienteDesc();

    /**
     * Agrupa as OS da fila por status.
     * A ordem dos grupos segue o ciclo de vida da OS:
     * ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.
     */
    Map<OrdemServicoModel.StatusOrdemServico, List<OrdemServicoModel>> listarAgrupadoPorStatus();
}