package br.com.picarauto.controller;

/**
 * 
 * @author Caio4breu
 */
import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.service.IUsuarioService;

public class UsuarioController extends GenericController<UsuarioModel, IUsuarioService> {
    public UsuarioController(IUsuarioService service) {
        super(service);
    }
}