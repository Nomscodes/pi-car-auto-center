/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public interface IHistoricoVeiculoRepository {

    void save(Integer idPessoa, Integer idVeiculo, LocalDate dataInicio, LocalDate dataFim);

    List<Integer> findIdVeiculoByIdPessoa(Integer idPessoa);

    List<Integer> findIdPessoaByIdVeiculo(Integer idVeiculo);

    boolean existsByIdPessoaAndIdVeiculo(Integer idPessoa, Integer idVeiculo);

    void delete(Integer idPessoa, Integer idVeiculo, LocalDate dataInicio);
}
