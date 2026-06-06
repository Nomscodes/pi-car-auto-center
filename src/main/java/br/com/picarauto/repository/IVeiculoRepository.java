package br.com.picarauto.repository;

import br.com.picarauto.model.VeiculoModel;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data para VeiculoModel.
 * existsByPlaca: gerado automaticamente pelo Spring Data por convenção de nome.
 */
@Repository
public interface IVeiculoRepository extends IGenericRepository<VeiculoModel> {
    boolean existsByPlaca(String placa);
    VeiculoModel findByPlaca(String placa);
}
