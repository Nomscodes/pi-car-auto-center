package br.com.picarauto.repository;

import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaRepository implements IPecaRepository {

    // ─── Mapeamento ResultSet → PecaModel ────────────────────────────────────

    private PecaModel mapRow(ResultSet rs) throws SQLException {
        PecaModel p = new PecaModel();
        p.setId(rs.getInt("id"));
        p.setAtivo(rs.getBoolean("ativo"));
        p.setNome(rs.getString("nome"));
        p.setQuantidade(rs.getInt("quantidade"));
        p.setValorUnitario(rs.getBigDecimal("valor_unitario"));

        Timestamp dataHoraCriacao = rs.getTimestamp("data_hora_criacao");
        if (dataHoraCriacao != null) {
            p.setDataHoraCriacao(new java.util.Date(dataHoraCriacao.getTime()));
        }
        return p;
    }

    // ─── Consultas genéricas ──────────────────────────────────────────────────

    @Override
    public PecaModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM peca WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar peça.", e);
        }
    }

    @Override
    public List<PecaModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM peca WHERE ativo = 1 ORDER BY nome ASC";
        List<PecaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar peças.", e);
        }
    }

    // ─── Persistência (INSERT / UPDATE) ──────────────────────────────────────

    @Override
    public PecaModel save(PecaModel p) {
        if (p.getId() == null) {
            String sql = "INSERT INTO peca (nome, quantidade, valor_unitario, ativo, data_hora_criacao) "
                       + "VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getNome());
                ps.setInt(2, p.getQuantidade());
                ps.setBigDecimal(3, p.getValorUnitario());
                ps.setBoolean(4, p.isAtivo());
                ps.setTimestamp(5, new Timestamp(p.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) p.setId(keys.getInt(1));
                return p;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "peça");
                return null;
            }
        } else {
            String sql = "UPDATE peca SET nome = ?, quantidade = ?, valor_unitario = ?, ativo = ? "
                       + "WHERE id = ?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getNome());
                ps.setInt(2, p.getQuantidade());
                ps.setBigDecimal(3, p.getValorUnitario());
                ps.setBoolean(4, p.isAtivo());
                ps.setInt(5, p.getId());
                ps.executeUpdate();
                return p;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "peça");
                return null;
            }
        }
    }

    // ─── Verificações de existência ───────────────────────────────────────────

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM peca WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar peça por ID.", e);
        }
    }

    @Override
    public boolean existsByNome(String nome) {
        String sql = "SELECT 1 FROM peca WHERE nome = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar nome da peça.", e);
        }
    }
}