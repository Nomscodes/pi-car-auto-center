package br.com.picarauto.repository;

/**
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.VeiculoModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IVeiculoRepository extends IGenericRepository<VeiculoModel> {
    boolean existsByPlaca(String placa);
    boolean existsByChassi(String chassi);
}