package br.com.picarauto.service;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IOrdemServicoRepository;
import br.com.picarauto.validation.IOrdemServicoValidation;

public interface IOrdemServicoService extends IGenericService<OrdemServicoModel, IOrdemServicoRepository, IOrdemServicoValidation> {}