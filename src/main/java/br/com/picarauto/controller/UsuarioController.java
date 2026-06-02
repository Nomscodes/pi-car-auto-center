package br.com.picarauto.controller;

import br.com.picarauto.controller.mapper.IUsuarioMapper;
import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.dto.UsuarioDTO;
import br.com.picarauto.service.IUsuarioService;

public class UsuarioController extends GenericController<UsuarioModel, UsuarioDTO, IUsuarioService, IUsuarioMapper> {

    public UsuarioController(IUsuarioService service, IUsuarioMapper mapper) {
        super(service, mapper);
    }
}