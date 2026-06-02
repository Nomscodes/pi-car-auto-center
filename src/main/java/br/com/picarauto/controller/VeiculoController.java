package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IVeiculoMapper;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.dto.VeiculoDTO;
import br.com.picarauto.service.IVeiculoService;

public class VeiculoController extends GenericController<VeiculoModel, VeiculoDTO, IVeiculoService, IVeiculoMapper> {

    public VeiculoController(IVeiculoService service, IVeiculoMapper mapper) {
        super(service, mapper);
    }
}