package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoRepository implements IServicoRepository {

    private ServicoModel mapRow(ResultSet rs) throws SQLException {
        ServicoModel s = new ServicoModel();
        s.setId(rs.getInt("id"));
        s.setAtivo(rs.getBoolean("ativo"));
        s.setNome(rs.getString("nome"));
        s.setDescricao(rs.getString("descricao"));
        s.setPreco(rs.getBigDecimal("preco"));
        return s;
    }

    @Override
    public ServicoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM servico WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar serviço.", e);
        }
    }

    @Override
    public List<ServicoModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM servico WHERE ativo = 1";
        List<ServicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar serviços.", e);
        }
    }

    @Override
    public ServicoModel save(ServicoModel s) {
        if (s.getId() == null) {
            String sql = "INSERT INTO servico (nome, descricao, preco, ativo, data_hora_criacao) VALUES (?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, s.getNome());
                ps.setString(2, s.getDescricao());
                ps.setBigDecimal(3, s.getPreco());
                ps.setBoolean(4, s.isAtivo());
                ps.setTimestamp(5, new Timestamp(s.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) s.setId(keys.getInt(1));
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço");
                return null;
            }
        } else {
            String sql = "UPDATE servico SET nome=?, descricao=?, preco=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, s.getNome());
                ps.setString(2, s.getDescricao());
                ps.setBigDecimal(3, s.getPreco());
                ps.setBoolean(4, s.isAtivo());
                ps.setInt(5, s.getId());
                ps.executeUpdate();
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM servico WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar serviço.", e);
        }
    }

    @Override
    public boolean existsByNome(String nome) {
        String sql = "SELECT 1 FROM servico WHERE nome = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar nome do serviço.", e);
        }
    }
}