package br.com.picarauto.repository;

/**
 *
 * @author Gabriel
 */
import br.com.picarauto.model.exception.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HistoricoVeiculoRepository implements IHistoricoVeiculoRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Long idPessoa, Long idVeiculo, LocalDate dataInicio, LocalDate dataFim) {
        em.createNativeQuery("""
                INSERT INTO historicoVeiculo (idPessoa, idVeiculo, dataInicio, dataFim)
                VALUES (:idPessoa, :idVeiculo, :dataInicio, :dataFim)
                """)
                .setParameter("idPessoa", idPessoa)
                .setParameter("idVeiculo", idVeiculo)
                .setParameter("dataInicio", dataInicio)
                .setParameter("dataFim", dataFim)
                .executeUpdate();
    }

    @Override
    public List<Long> findIdVeiculoByIdPessoa(Long idPessoa) {
        return em.createNativeQuery("""
                SELECT idVeiculo FROM historicoVeiculo WHERE idPessoa = :idPessoa
                """)
                .setParameter("idPessoa", idPessoa)
                .getResultList();
    }

    @Override
    public List<Long> findIdPessoaByIdVeiculo(Long idVeiculo) {
        return em.createNativeQuery("""
                SELECT idPessoa FROM historicoVeiculo WHERE idVeiculo = :idVeiculo
                """)
                .setParameter("idVeiculo", idVeiculo)
                .getResultList();
    }

    @Override
    public boolean existsByIdPessoaAndIdVeiculo(Long idPessoa, Long idVeiculo) {
        Number count = (Number) em.createNativeQuery("""
                SELECT COUNT(1) FROM historicoVeiculo
                WHERE idPessoa = :idPessoa AND idVeiculo = :idVeiculo
                """)
                .setParameter("idPessoa", idPessoa)
                .setParameter("idVeiculo", idVeiculo)
                .getSingleResult();
        return count.longValue() > 0;
    }

    @Override
    @Transactional
    public void delete(Long idPessoa, Long idVeiculo, LocalDate dataInicio) {
        em.createNativeQuery("""
                DELETE FROM historicoVeiculo
                WHERE idPessoa = :idPessoa AND idVeiculo = :idVeiculo AND dataInicio = :dataInicio
                """)
                .setParameter("idPessoa", idPessoa)
                .setParameter("idVeiculo", idVeiculo)
                .setParameter("dataInicio", dataInicio)
                .executeUpdate();
    }
}