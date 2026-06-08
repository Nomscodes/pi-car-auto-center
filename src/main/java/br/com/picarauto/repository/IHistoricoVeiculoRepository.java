package br.com.picarauto.repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 
 * @author Gabriel
 */
public interface IHistoricoVeiculoRepository {

    void save(Long idPessoa, Long idVeiculo, LocalDate dataInicio, LocalDate dataFim);
    List<Long> findIdVeiculoByIdPessoa(Long idPessoa);
    List<Long> findIdPessoaByIdVeiculo(Long idVeiculo);
    boolean existsByIdPessoaAndIdVeiculo(Long idPessoa, Long idVeiculo);
    void delete(Long idPessoa, Long idVeiculo, LocalDate dataInicio);
}