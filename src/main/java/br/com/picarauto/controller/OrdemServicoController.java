package br.com.picarauto.controller;

/**
 *
 * @author Caio4breu
 */
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.service.IOrdemServicoService;
import br.com.picarauto.util.FilaOS;

@Component
public class OrdemServicoController extends GenericController<OrdemServicoModel, IOrdemServicoService> {

    public OrdemServicoController(IOrdemServicoService service) {
        super(service);
    }

    /**
     * Retorna todas as OS ativas com placaVeiculo e nomeCliente populados. Use
     * este método para popular PanelListaOS.
     */
    public List<OrdemServicoModel> findAllEnriquecido() {
        return service.findAllActiveEnriquecido();
    }
    
    /**
     * Expõe a FilaOS para os ordenadores do Template Method.
     * Usada pela view na busca binária por data (OrdenadorPorData).
     */
    public FilaOS getFilaEspera() {
        return service.getFilaEspera();
    }
    
    // NOVO: busca por id usando ArvoreOS — O(log n) em vez de O(n) na fila
    public OrdemServicoModel buscarPorId(Long id) {
        return service.buscarPorId(id);
    }

    // NOVO: busca por placa exata usando TabelaHashOS — O(1)
    public OrdemServicoModel buscarPorPlacaExata(String placa) {
        return service.buscarPorPlacaExata(placa);
    }
}
