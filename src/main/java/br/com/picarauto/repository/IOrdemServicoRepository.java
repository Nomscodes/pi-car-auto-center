package br.com.picarauto.repository;

import br.com.picarauto.model.OrdemServicoModel;

public interface IOrdemServicoRepository extends IGenericRepository<OrdemServicoModel> {
    Long gerarProximoNumero();
}