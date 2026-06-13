package br.com.picarauto.service;

import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.repository.IPessoaFisicaRepository;
import br.com.picarauto.validation.IPessoaFisicaValidation;

/**
 *
 * @author Caio4breu
 */
public interface IPessoaFisicaService extends IGenericService<PessoaFisicaModel, IPessoaFisicaRepository, IPessoaFisicaValidation> {}