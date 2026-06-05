package br.com.picarauto.repository;

import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public class ServicoInternoRepository implements IServicoInternoRepository {

    private ServicoInternoModel mapRow(ResultSet rs) throws SQLException {
        ServicoInternoModel s = new ServicoInternoModel();
        s.setId(rs.getInt("idServicoInterno"));
        s.setAtivo(true);
        s.setDescricao(rs.getString("descricao"));
        s.setValorCobrado(rs.getDouble("valorCobrado"));
        return s;
    }

    @Override
    public ServicoInternoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT idServicoInterno, descricao, valorCobrado FROM servicosInternos WHERE idServicoInterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar serviço interno.", e);
        }
    }

    @Override
    public List<ServicoInternoModel> findAllByAtivoTrue() {
        String sql = "SELECT idServicoInterno, descricao, valorCobrado FROM servicosInternos";
        List<ServicoInternoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar serviços internos.", e);
        }
    }

    @Override
    public ServicoInternoModel save(ServicoInternoModel s) {
        if (s.getId() == null) {
            String sql = "INSERT INTO servicosInternos (descricao, valorCobrado) VALUES (?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, s.getDescricao());
                ps.setDouble(2, s.getValorCobrado());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) s.setId(keys.getInt(1));
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço interno");
                return null;
            }
        } else {
            String sql = "UPDATE servicosInternos SET descricao=?, valorCobrado=? WHERE idServicoInterno=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, s.getDescricao());
                ps.setDouble(2, s.getValorCobrado());
                ps.setInt(3, s.getId());
                ps.executeUpdate();
                return s;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "serviço interno");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM servicosInternos WHERE idServicoInterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar serviço interno.", e);
        }
    }

    @Override
    public boolean existsByDescricao(String descricao) {
        String sql = "SELECT 1 FROM servicosInternos WHERE descricao = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar descrição do serviço interno.", e);
        }
    }
}