/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */

public class MarcaRepository implements IMarcaRepository {
    
    //Mapeamento ResultSet → MarcaModel 

    private MarcaModel mapRow(ResultSet rs) throws SQLException {
        MarcaModel m = new MarcaModel();
        m.setId(rs.getInt("id"));
        m.setAtivo(rs.getBoolean("ativo"));
        m.setNome(rs.getString("nome"));
        return m;
    }

    //Consultas genéricas 
    @Override
    public MarcaModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM marca WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar marca.", e);
        }
    }

    @Override
    public List<MarcaModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM marca WHERE ativo = 1";
        List<MarcaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar marcas.", e);
        }
    }

    //Persistência (INSERT / UPDATE) 
    @Override
    public MarcaModel save(MarcaModel m) {
        if (m.getId() == null) {
            String sql = "INSERT INTO marca (nome, ativo, data_hora_criacao) VALUES (?, ?, ?)";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNome());
                ps.setBoolean(2, m.isAtivo());
                ps.setTimestamp(3, new Timestamp(m.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    m.setId(keys.getInt(1));
                }
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "marca");
                return null;
            }
        } else {
            String sql = "UPDATE marca SET nome = ?, ativo = ? WHERE id = ?";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getNome());
                ps.setBoolean(2, m.isAtivo());
                ps.setInt(3, m.getId());
                ps.executeUpdate();
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "marca");
                return null;
            }
        }
    }

    //Verificações de existência 
    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM marca WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar marca por ID.", e);
        }
    }

    @Override
    public boolean existsByNome(String nome) {
        String sql = "SELECT 1 FROM marca WHERE nome = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar nome da marca.", e);
        }
    }
}
