/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.ColaboradorModel;

/**
 *
 * @author Gabriel
 */
@Repository
public interface IColaboradorRepository extends IGenericRepository<ColaboradorModel> {

    boolean existsByEmaul(String email);
}
