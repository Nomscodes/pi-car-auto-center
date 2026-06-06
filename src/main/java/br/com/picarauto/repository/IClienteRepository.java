package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para ClienteModel.
 * existsByCpf: Spring Data resolve por convenção de nome (campo cpf na herança PessoaFisicaModel).
 * Para CPF específico, use IPessoaFisicaRepository.
 */
@Repository
public interface IClienteRepository extends IGenericRepository<ClienteModel> {
}
