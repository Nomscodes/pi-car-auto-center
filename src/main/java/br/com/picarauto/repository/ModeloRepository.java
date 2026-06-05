/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ModeloRepository implements IModeloRepository{

    //Mapeamento ResultSet → ModeloModel
    private ModeloModel mapRow(ResultSet rs) throws SQLException {
        ModeloModel m = new ModeloModel();
        m.setId(rs.getInt("id"));
        m.setAtivo(rs.getBoolean("ativo"));
        m.setNomeModelo(rs.getString("nome_modelo"));
        m.setAnoModelo(rs.getInt("ano_modelo"));
        return m;
    }

    //Consultas genéricas
    @Override
    public ModeloModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM modelo WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar modelo.", e);
        }
    }

    @Override
    public List<ModeloModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM modelo WHERE ativo = 1";
        List<ModeloModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar modelos.", e);
        }
    }

    //Persistência (INSERT / UPDATE) 
    @Override
    public ModeloModel save(ModeloModel m) {
        if (m.getId() == null) {
            String sql = "INSERT INTO modelo (nome_modelo, ano_modelo, ativo, data_hora_criacao) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNomeModelo());
                ps.setInt(2, m.getAnoModelo());
                ps.setBoolean(3, m.isAtivo());
                ps.setTimestamp(4, new Timestamp(m.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    m.setId(keys.getInt(1));
                }
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "modelo");
                return null;
            }
        } else {
            String sql = "UPDATE modelo SET nome_modelo = ?, ano_modelo = ?, ativo = ? WHERE id = ?";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getNomeModelo());
                ps.setInt(2, m.getAnoModelo());
                ps.setBoolean(3, m.isAtivo());
                ps.setInt(4, m.getId());
                ps.executeUpdate();
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "modelo");
                return null;
            }
        }
    }

    //Verificações de existência
    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM modelo WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar modelo por ID.", e);
        }
    }

    @Override
    public boolean existsByNomeModelo(String nomeModelo) {
        String sql = "SELECT 1 FROM modelo WHERE nome_modelo = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeModelo);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar nome do modelo.", e);
        }
    }
}
