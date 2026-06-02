package br.com.picarauto.repository;

import br.com.picarauto.model.UsuarioModel;

public interface IUsuarioRepository extends IGenericRepository<UsuarioModel> {
    boolean existsByLogin(String login);
    UsuarioModel findByLogin(String login);
}