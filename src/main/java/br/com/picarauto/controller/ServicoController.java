package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IServicoMapper;
import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.model.dto.ServicoDTO;
import br.com.picarauto.service.IServicoService;

public class ServicoController extends GenericController<ServicoModel, ServicoDTO, IServicoService, IServicoMapper> {

    public ServicoController(IServicoService service, IServicoMapper mapper) {
        super(service, mapper);
    }
}