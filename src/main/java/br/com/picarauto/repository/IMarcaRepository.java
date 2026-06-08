/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.MarcaModel;

/**
 *
 * @author Gabriel
 */
public interface IMarcaRepository extends IGenericRepository<MarcaModel>{
    
    boolean existsByNome(String nome);
}
