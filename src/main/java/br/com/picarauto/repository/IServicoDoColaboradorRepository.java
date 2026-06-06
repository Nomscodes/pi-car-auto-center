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
public interface IServicoDoColaboradorRepository {

    void save(Integer idColaborador, Integer idServicoInterno, LocalDate dataServico);

    List<Integer> findIdServicoInternoByIdColaborador(Integer idColaborador);

    List<Integer> findIdColaboradorByIdServicoInterno(Integer idServicoInterno);

    boolean existsByIdColaboradorAndIdServicoInterno(Integer idColaborador, Integer idServicoInterno);

    void delete(Integer idColaborador, Integer idServicoInterno, LocalDate dataServico);
}
