package br.com.picarauto.service;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.repository.IUsuarioRepository;
import br.com.picarauto.validation.IUsuarioValidation;

public interface IUsuarioService extends IGenericService<UsuarioModel, IUsuarioRepository, IUsuarioValidation> {}