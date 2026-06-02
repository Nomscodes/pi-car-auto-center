package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IClienteMapper;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.dto.ClienteDTO;
import br.com.picarauto.service.IClienteService;

public class ClienteController extends GenericController<ClienteModel, ClienteDTO, IClienteService, IClienteMapper> {

    public ClienteController(IClienteService service, IClienteMapper mapper) {
        super(service, mapper);
    }
}