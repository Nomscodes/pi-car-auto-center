package br.com.picarauto.service;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.repository.IUsuarioRepository;
import br.com.picarauto.validation.IUsuarioValidation;

public class UsuarioService extends GenericService<UsuarioModel, IUsuarioRepository, IUsuarioValidation>
        implements IUsuarioService {

    public UsuarioService(IUsuarioRepository repository, IUsuarioValidation validation) {
        super(repository, validation);
    }
}