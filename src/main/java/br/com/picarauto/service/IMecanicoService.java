package br.com.picarauto.service;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.repository.IMecanicoRepository;
import br.com.picarauto.validation.IMecanicoValidation;

public interface IMecanicoService extends IGenericService<MecanicoModel, IMecanicoRepository, IMecanicoValidation> {}