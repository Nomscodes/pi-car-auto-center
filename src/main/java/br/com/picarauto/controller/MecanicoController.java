package br.com.picarauto.controller;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.service.IMecanicoService;

public class MecanicoController extends GenericController<MecanicoModel, IMecanicoService> {
    public MecanicoController(IMecanicoService service) {
        super(service);
    }
}