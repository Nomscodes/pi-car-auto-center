package br.com.picarauto.repository;

import br.com.picarauto.model.VeiculoModel;

public interface IVeiculoRepository extends IGenericRepository<VeiculoModel> {
    boolean existsByPlaca(String placa);
}