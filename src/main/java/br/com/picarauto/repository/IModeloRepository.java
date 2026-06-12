/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.repository;

import java.util.List;

import br.com.picarauto.model.ModeloModel;

/**
 *
 * @author Gabriel
 */
public interface IModeloRepository extends IGenericRepository<ModeloModel> {

    boolean existsByNomeModelo(String nomeModelo);

    List<ModeloModel> findAllByIdMarcaAndAtivoTrue(Long idMarca);
}
