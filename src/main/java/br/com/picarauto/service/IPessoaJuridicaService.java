package br.com.picarauto.service;

import br.com.picarauto.model.PessoaJuridicaModel;
import br.com.picarauto.repository.IPessoaJuridicaRepository;
import br.com.picarauto.validation.IPessoaJuridicaValidation;

/**
 *
 * @author Caio4breu
 */
public interface IPessoaJuridicaService extends IGenericService<PessoaJuridicaModel, IPessoaJuridicaRepository, IPessoaJuridicaValidation> {}