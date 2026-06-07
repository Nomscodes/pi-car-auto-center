package br.com.picarauto.repository;

/**
 * Repository para a tabela de relacionamento {@code servicosItens}.
 *
 * Vincula um item de serviço interno ({@code itemServicoInterno})
 * ao seu respectivo serviço do catálogo ({@code servicosInternos}).
 *
 * Chave composta: idServicoInterno + idItemServicoInterno
 *
 * @author Caio4breu
 */
import br.com.picarauto.model.ItemServicoInternoModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ServicosItensRepository implements IServicosItensRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Long idServicoInterno, Long idItemServicoInterno, LocalDate dataExecucao) {
        em.createNativeQuery("""
                INSERT INTO servicosItens (idServicoInterno, idItemServicoInterno, dataExecucao)
                VALUES (:idServico, :idItem, :dataExecucao)
                """)
                .setParameter("idServico", idServicoInterno)
                .setParameter("idItem", idItemServicoInterno)
                .setParameter("dataExecucao", dataExecucao)
                .executeUpdate();
    }

    @Override
    public List<ItemServicoInternoModel> findAllByIdServicoInterno(Long idServicoInterno) {
        return em.createQuery("""
                SELECT i FROM ItemServicoInternoModel i
                JOIN servicosItens si ON si.idItemServicoInterno = i.id
                WHERE si.idServicoInterno = :idServico
                ORDER BY i.id ASC
                """, ItemServicoInternoModel.class)
                .setParameter("idServico", idServicoInterno)
                .getResultList();
    }

    @Override
    public List<Long> findIdServicoInternoByIdItemServicoInterno(Long idItemServicoInterno) {
        return em.createNativeQuery("""
                SELECT idServicoInterno FROM servicosItens
                WHERE idItemServicoInterno = :idItem
                """)
                .setParameter("idItem", idItemServicoInterno)
                .getResultList();
    }

    @Override
    public boolean existsByServicoInternoAndItemServicoInterno(Long idServicoInterno, Long idItemServicoInterno) {
        Number count = (Number) em.createNativeQuery("""
                SELECT COUNT(1) FROM servicosItens
                WHERE idServicoInterno = :idServico AND idItemServicoInterno = :idItem
                """)
                .setParameter("idServico", idServicoInterno)
                .setParameter("idItem", idItemServicoInterno)
                .getSingleResult();
        return count.longValue() > 0;
    }

    @Override
    public void delete(Long idServicoInterno, Long idItemServicoInterno) {
        em.createNativeQuery("""
                DELETE FROM servicosItens
                WHERE idServicoInterno = :idServico AND idItemServicoInterno = :idItem
                """)
                .setParameter("idServico", idServicoInterno)
                .setParameter("idItem", idItemServicoInterno)
                .executeUpdate();
    }
}