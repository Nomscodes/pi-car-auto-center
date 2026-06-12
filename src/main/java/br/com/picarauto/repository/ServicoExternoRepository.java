package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public class ServicoExternoRepository implements IServicoExternoRepository {

    private ServicoExternoModel mapRow(ResultSet rs) throws SQLException {
        ServicoExternoModel s = new ServicoExternoModel();
        s.setId(rs.getInt("idServicoExterno"));
        s.setAtivo(true);
        s.setDescricao(rs.getString("descricao"));
        s.setValorCobrado(rs.getDouble("valorCobrado"));
        return s;
    }

    @Override
    public ServicoExternoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT idServicoExterno, descricao, valorCobrado FROM servicoExterno WHERE idServicoExterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar serviço externo.", e);
        }
    }

    @Override
    public List<ServicoExternoModel> findAllByAtivoTrue() {
        String sql = "SELECT idServicoExterno, descricao, valorCobrado FROM servicoExterno";
        List<ServicoExternoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar serviços externos.", e);
        }
    }

    @Override
    public ServicoExternoModel save(ServicoExternoModel s) {
        if (s.getId() == null) {
            String sql = "INSERT INTO servicoExterno (descricao, valorCobrado) VALUES (?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, s.getDescricao());
                ps.setDouble(2, s.getValorCobrado());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) s.setId(keys.getInt(1));
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço externo");
                return null;
            }
        } else {
            String sql = "UPDATE servicoExterno SET descricao=?, valorCobrado=? WHERE idServicoExterno=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, s.getDescricao());
                ps.setDouble(2, s.getValorCobrado());
                ps.setInt(3, s.getId());
                ps.executeUpdate();
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço externo");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM servicoExterno WHERE idServicoExterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar serviço externo.", e);
        }
    }

    @Override
    public boolean existsByDescricao(String descricao) {
        String sql = "SELECT 1 FROM servicoExterno WHERE descricao = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar descrição do serviço externo.", e);
        }
    }
}