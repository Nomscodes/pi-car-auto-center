package br.com.picarauto.repository;

/**
 *
 * @author Gabriel
 */
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ServicoDoColaboradorRepository implements IServicoDoColaboradorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Long idColaborador, Long idServicoInterno, LocalDate dataServico) {
        em.createNativeQuery("""
                INSERT INTO servicosDoColaborador (idColaborador, idServicoInterno, dataServico)
                VALUES (:idColaborador, :idServicoInterno, :dataServico)
                """)
                .setParameter("idColaborador", idColaborador)
                .setParameter("idServicoInterno", idServicoInterno)
                .setParameter("dataServico", dataServico)
                .executeUpdate();
    }

    @Override
    public List<Long> findIdServicoInternoByIdColaborador(Long idColaborador) {
        return em.createNativeQuery("""
                SELECT idServicoInterno FROM servicosDoColaborador
                WHERE idColaborador = :idColaborador
                """)
                .setParameter("idColaborador", idColaborador)
                .getResultList();
    }

    @Override
    public List<Long> findIdColaboradorByIdServicoInterno(Long idServicoInterno) {
        return em.createNativeQuery("""
                SELECT idColaborador FROM servicosDoColaborador
                WHERE idServicoInterno = :idServicoInterno
                """)
                .setParameter("idServicoInterno", idServicoInterno)
                .getResultList();
    }

    @Override
    public boolean existsByIdColaboradorAndIdServicoInterno(Long idColaborador, Long idServicoInterno) {
        Number count = (Number) em.createNativeQuery("""
                SELECT COUNT(1) FROM servicosDoColaborador
                WHERE idColaborador = :idColaborador AND idServicoInterno = :idServicoInterno
                """)
                .setParameter("idColaborador", idColaborador)
                .setParameter("idServicoInterno", idServicoInterno)
                .getSingleResult();
        return count.longValue() > 0;
    }

    @Override
    @Transactional
    public void delete(Long idColaborador, Long idServicoInterno, LocalDate dataServico) {
        em.createNativeQuery("""
                DELETE FROM servicosDoColaborador
                WHERE idColaborador = :idColaborador
                AND idServicoInterno = :idServicoInterno
                AND dataServico = :dataServico
                """)
                .setParameter("idColaborador", idColaborador)
                .setParameter("idServicoInterno", idServicoInterno)
                .setParameter("dataServico", dataServico)
                .executeUpdate();
    }
}