/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class FornecedorRepository implements IFornecedorRepository {
    
    //Mapeamento ResultSet → FornecedorModel
    private FornecedorModel mapRow(ResultSet rs) throws SQLException {
        FornecedorModel f = new FornecedorModel();
        f.setId(rs.getInt("idFornecedor"));
        f.setAtivo(true);
        f.setNomeFornecedor(rs.getString("nomeFornecedor"));
        f.setTelefone(rs.getString("telefone"));
        f.setCnpj(rs.getString("cnpj"));
        f.setEmail(rs.getString("email"));
        return f;
    }

    //Consultas genéricas
    @Override
    public FornecedorModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM fornecedor WHERE idFornecedor = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar fornecedor.", e);
        }
    }

    @Override
    public List<FornecedorModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM fornecedor";
        List<FornecedorModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar fornecedores.", e);
        }
    }

    //Persistência (INSERT / UPDATE)
    @Override
    public FornecedorModel save(FornecedorModel f) {
        if (f.getId() == null) {
            String sql = "INSERT INTO fornecedor (nomeFornecedor, telefone, cnpj, email) VALUES (?, ?, ?, ?)";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, f.getNomeFornecedor());
                ps.setString(2, f.getTelefone());
                ps.setObject(3, f.getCnpj());
                ps.setString(4, f.getEmail());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    f.setId(keys.getInt(1));
                }
                return f;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "fornecedor");
                return null;
            }
        } else {
            String sql = "UPDATE fornecedor SET nomeFornecedor = ?, telefone = ?, cnpj = ?, email = ? WHERE idFornecedor = ?";
            try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, f.getNomeFornecedor());
                ps.setString(2, f.getTelefone());
                ps.setObject(3, f.getCnpj());
                ps.setString(4, f.getEmail());
                ps.setInt(5, f.getId());
                ps.executeUpdate();
                return f;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "fornecedor");
                return null;
            }
        }
    }

    //Verificações de existência
    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM fornecedor WHERE idFornecedor = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar fornecedor por ID.", e);
        }
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        String sql = "SELECT 1 FROM fornecedor WHERE cnpj = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar CNPJ do fornecedor.", e);
        }
    }

    @Override
    public boolean existsByTelefone(String telefone) {
        String sql = "SELECT 1 FROM fornecedor WHERE telefone = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telefone);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar telefone do fornecedor.", e);
        }
    }
}
