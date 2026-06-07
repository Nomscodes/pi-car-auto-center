package br.com.picarauto.service;

import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.repository.IVeiculoRepository;
import br.com.picarauto.validation.IVeiculoValidation;

public interface IVeiculoService extends IGenericService<VeiculoModel, IVeiculoRepository, IVeiculoValidation> {}