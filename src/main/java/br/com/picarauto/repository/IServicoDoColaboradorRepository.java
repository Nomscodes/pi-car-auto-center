package br.com.picarauto.repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 
 * @author Gabriel
 */
public interface IServicoDoColaboradorRepository {

    void save(Long idColaborador, Long idServicoInterno, LocalDate dataServico);

    List<Long> findIdServicoInternoByIdColaborador(Long idColaborador);

    List<Long> findIdColaboradorByIdServicoInterno(Long idServicoInterno);

    boolean existsByIdColaboradorAndIdServicoInterno(Long idColaborador, Long idServicoInterno);

    void delete(Long idColaborador, Long idServicoInterno, LocalDate dataServico);
}