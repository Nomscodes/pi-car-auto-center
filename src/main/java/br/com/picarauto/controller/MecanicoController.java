package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IMecanicoMapper;
import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.dto.MecanicoDTO;
import br.com.picarauto.service.IMecanicoService;

public class MecanicoController extends GenericController<MecanicoModel, MecanicoDTO, IMecanicoService, IMecanicoMapper> {

    public MecanicoController(IMecanicoService service, IMecanicoMapper mapper) {
        super(service, mapper);
    }
}