package br.com.picarauto.validation;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.repository.IClienteRepository;

public interface IClienteValidation extends IGenericValidation<ClienteModel, IClienteRepository> {}