package br.com.picarauto.service;

import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.repository.IServicoRepository;
import br.com.picarauto.validation.IServicoValidation;

public interface IServicoService extends IGenericService<ServicoModel, IServicoRepository, IServicoValidation> {}