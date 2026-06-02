package br.com.picarauto.service;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.repository.IClienteRepository;
import br.com.picarauto.validation.IClienteValidation;

public interface IClienteService extends IGenericService<ClienteModel, IClienteRepository, IClienteValidation> {}