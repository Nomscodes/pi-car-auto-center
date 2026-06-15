package br.com.picarauto.service;

import java.util.List;
import java.util.Map;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.validation.IOrdemServicoValidation;
import br.com.picarauto.util.FilaOS;

/**
 *
 * @author Caio4breu
 */
public interface IOrdemServicoService extends IGenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation> {

    /**
     * Retorna as OS da fila ordenadas por ID em ordem crescente.
     */
    List<OrdemServicoModel> listarOrdenadoPorIdAsc();

    /**
     * Retorna as OS da fila ordenadas por ID em ordem decrescente.
     */
    List<OrdemServicoModel> listarOrdenadoPorIdDesc();

    /**
     * Retorna as OS da fila ordenadas por nome do cliente de A a Z.
     */
    List<OrdemServicoModel> listarOrdenadoPorNomeClienteAsc();

    /**
     * Retorna as OS da fila ordenadas por nome do cliente de Z a A.
     */
    List<OrdemServicoModel> listarOrdenadoPorNomeClienteDesc();

    /**
     * Agrupa as OS da fila por status. A ordem dos grupos segue o ciclo de vida
     * da OS: ORCAMENTO → EXECUCAO → PAGAMENTO → FINALIZADO.
     */
    Map<OrdemServicoModel.StatusOrdemServico, List<OrdemServicoModel>> listarAgrupadoPorStatus();

    /**
     * Busca todas as OS ativas do banco e enriquece cada uma com placaVeiculo e
     * nomeCliente nos campos @Transient. Use este método para popular tabelas
     * na View — não usa a FilaOS.
     */
    List<OrdemServicoModel> findAllActiveEnriquecido();
    /**
     * Retorna a FilaOS em memória.
     * Usada pelo Template Method (OrdenadorPorData) na busca binária por data.
     */
    FilaOS getFilaEspera();
    
    // NOVO: expõe busca por id via ArvoreOS — O(log n)
    OrdemServicoModel buscarPorId(Long id);

    // NOVO: expõe busca por placa exata via TabelaHashOS — O(1)
    OrdemServicoModel buscarPorPlacaExata(String placa);
    
}
