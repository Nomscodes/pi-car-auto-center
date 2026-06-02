package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IOrdemServicoMapper;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.dto.OrdemServicoDTO;
import br.com.picarauto.service.IOrdemServicoService;

public class OrdemServicoController extends GenericController<OrdemServicoModel, OrdemServicoDTO, IOrdemServicoService, IOrdemServicoMapper> {

    public OrdemServicoController(IOrdemServicoService service, IOrdemServicoMapper mapper) {
        super(service, mapper);
    }
}