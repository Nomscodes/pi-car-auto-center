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
     * Expõe a FilaOS em memória para uso nos ordenadores (Template Method).
     * Usada pela PanelListaOS para busca binária por data.
     */
    public FilaOS getFilaEspera() {
        return service.getFilaEspera();
    }
}