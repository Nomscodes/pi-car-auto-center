package br.com.picarauto.repository;

import br.com.picarauto.model.FuncaoColaboradorModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public class FuncaoColaboradorRepository implements IFuncaoColaboradorRepository {

    private FuncaoColaboradorModel mapRow(ResultSet rs) throws SQLException {
        FuncaoColaboradorModel f = new FuncaoColaboradorModel();
        f.setId(rs.getInt("idFuncao"));
        f.setAtivo(true); // funcaoColaborador não tem coluna ativo no schema; sempre ativo quando presente
        f.setFuncao(rs.getString("funcao"));
        return f;
    }

    @Override
    public FuncaoColaboradorModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM funcaoColaborador WHERE idFuncao = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar função do colaborador.", e);
        }
    }

    @Override
    public List<FuncaoColaboradorModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM funcaoColaborador ORDER BY idFuncao ASC";
        List<FuncaoColaboradorModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar funções do colaborador.", e);
        }
    }

    @Override
    public FuncaoColaboradorModel save(FuncaoColaboradorModel f) {
        if (f.getId() == null) {
            String sql = "INSERT INTO funcaoColaborador (funcao) VALUES (?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, f.getFuncao());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) f.setId(keys.getInt(1));
                return f;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "função do colaborador");
                return null;
            }
        } else {
            String sql = "UPDATE funcaoColaborador SET funcao = ? WHERE idFuncao = ?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, f.getFuncao());
                ps.setInt(2, f.getId());
                ps.executeUpdate();
                return f;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "função do colaborador");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM funcaoColaborador WHERE idFuncao = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar função do colaborador.", e);
        }
    }

    @Override
    public boolean existsByFuncao(String funcao) {
        String sql = "SELECT 1 FROM funcaoColaborador WHERE funcao = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, funcao);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar função.", e);
        }
    }
}