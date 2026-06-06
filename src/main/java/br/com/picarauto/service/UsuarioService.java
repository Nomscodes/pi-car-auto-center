package br.com.picarauto.service;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.repository.IUsuarioRepository;
import br.com.picarauto.validation.IUsuarioValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Caio4breu
 */
@Service
public class UsuarioService extends GenericService<UsuarioModel, IUsuarioRepository, IUsuarioValidation>
        implements IUsuarioService {

    @Autowired
    public UsuarioService(IUsuarioRepository repository, IUsuarioValidation validation) {
        super(repository, validation);
    }
}
