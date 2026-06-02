package br.com.picarauto.repository;

import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MecanicoRepository implements IMecanicoRepository {

    private MecanicoModel mapRow(ResultSet rs) throws SQLException {
        MecanicoModel m = new MecanicoModel();
        m.setId(rs.getInt("id"));
        m.setAtivo(rs.getBoolean("ativo"));
        m.setNome(rs.getString("nome"));
        m.setCpf(rs.getString("cpf"));
        m.setEmail(rs.getString("email"));
        m.setTelefone(rs.getString("telefone"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setCrea(rs.getString("crea"));
        return m;
    }

    @Override
    public MecanicoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM mecanico WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar mecânico.", e);
        }
    }

    @Override
    public List<MecanicoModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM mecanico WHERE ativo = 1";
        List<MecanicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar mecânicos.", e);
        }
    }

    @Override
    public MecanicoModel save(MecanicoModel m) {
        if (m.getId() == null) {
            String sql = "INSERT INTO mecanico (nome, cpf, email, telefone, especialidade, crea, ativo, data_hora_criacao) VALUES (?,?,?,?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNome());
                ps.setString(2, m.getCpf());
                ps.setString(3, m.getEmail());
                ps.setString(4, m.getTelefone());
                ps.setString(5, m.getEspecialidade());
                ps.setString(6, m.getCrea());
                ps.setBoolean(7, m.isAtivo());
                ps.setTimestamp(8, new Timestamp(m.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) m.setId(keys.getInt(1));
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "mecânico");
                return null;
            }
        } else {
            String sql = "UPDATE mecanico SET nome=?, cpf=?, email=?, telefone=?, especialidade=?, crea=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, m.getNome());
                ps.setString(2, m.getCpf());
                ps.setString(3, m.getEmail());
                ps.setString(4, m.getTelefone());
                ps.setString(5, m.getEspecialidade());
                ps.setString(6, m.getCrea());
                ps.setBoolean(7, m.isAtivo());
                ps.setInt(8, m.getId());
                ps.executeUpdate();
                return m;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "mecânico");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM mecanico WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar mecânico.", e);
        }
    }

    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT 1 FROM mecanico WHERE cpf = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar CPF.", e);
        }
    }
}