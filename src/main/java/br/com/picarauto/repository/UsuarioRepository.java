package br.com.picarauto.repository;

import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements IUsuarioRepository {

    private UsuarioModel mapRow(ResultSet rs) throws SQLException {
        UsuarioModel u = new UsuarioModel();
        u.setId(rs.getInt("id"));
        u.setAtivo(rs.getBoolean("ativo"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setEmail(rs.getString("email"));
        u.setPerfil(rs.getString("perfil"));
        u.setSenhaHash(rs.getString("senha_hash"));
        return u;
    }

    @Override
    public UsuarioModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar usuário.", e);
        }
    }

    @Override
    public List<UsuarioModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM usuario WHERE ativo = 1";
        List<UsuarioModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar usuários.", e);
        }
    }

    @Override
    public UsuarioModel save(UsuarioModel u) {
        if (u.getId() == null) {
            String sql = "INSERT INTO usuario (nome, login, email, perfil, senha_hash, ativo, data_hora_criacao) VALUES (?,?,?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, u.getNome());
                ps.setString(2, u.getLogin());
                ps.setString(3, u.getEmail());
                ps.setString(4, u.getPerfil());
                ps.setString(5, u.getSenhaHash());
                ps.setBoolean(6, u.isAtivo());
                ps.setTimestamp(7, new Timestamp(u.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) u.setId(keys.getInt(1));
                return u;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "usuário");
                return null;
            }
        } else {
            String sql = "UPDATE usuario SET nome=?, login=?, email=?, perfil=?, senha_hash=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, u.getNome());
                ps.setString(2, u.getLogin());
                ps.setString(3, u.getEmail());
                ps.setString(4, u.getPerfil());
                ps.setString(5, u.getSenhaHash());
                ps.setBoolean(6, u.isAtivo());
                ps.setInt(7, u.getId());
                ps.executeUpdate();
                return u;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "usuário");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM usuario WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar usuário.", e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        String sql = "SELECT 1 FROM usuario WHERE login = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar login.", e);
        }
    }

    @Override
    public UsuarioModel findByLogin(String login) {
        String sql = "SELECT * FROM usuario WHERE login = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar usuário por login.", e);
        }
    }
}