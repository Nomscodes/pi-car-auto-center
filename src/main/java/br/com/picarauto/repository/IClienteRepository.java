package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ClienteModel;

public interface IClienteRepository extends IGenericRepository<ClienteModel> {
    boolean existsByCpf(String cpf);
}