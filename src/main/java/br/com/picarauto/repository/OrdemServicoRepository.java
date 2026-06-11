package br.com.picarauto.repository;

import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Caio4breu
 */
public class OrdemServicoRepository implements IOrdemServicoRepository {

    private OrdemServicoModel mapRow(ResultSet rs) throws SQLException {
        OrdemServicoModel os = new OrdemServicoModel();
        os.setId(rs.getInt("idOS"));
        os.setAtivo(true); // tabela não tem coluna ativo
        os.setDataAbertura(rs.getObject("dataAbertura", LocalDate.class));
        os.setDataFechamento(rs.getObject("dataFechamento", LocalDate.class));
        os.setStatus(OrdemServicoModel.StatusOrdemServico.valueOf(rs.getString("status").toUpperCase()));
        os.setValorTotal(rs.getDouble("valorTotal"));
        os.setObservacoes(rs.getString("observacoes"));
        os.setIdVeiculo(rs.getInt("idVeiculo"));
        return os;
    }

    @Override
    public OrdemServicoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT idOS, dataAbertura, dataFechamento, status, valorTotal, observacoes, idVeiculo FROM ordemDeServico WHERE idOS = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar ordem de serviço.", e);
        }
    }

    @Override
    public List<OrdemServicoModel> findAllByAtivoTrue() {
        String sql = "SELECT idOS, dataAbertura, dataFechamento, status, valorTotal, observacoes, idVeiculo FROM ordemDeServico ORDER BY dataAbertura DESC";
        List<OrdemServicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar ordens de serviço.", e);
        }
    }

    @Override
    public List<OrdemServicoModel> findAllByIdVeiculo(Integer idVeiculo) {
        String sql = "SELECT idOS, dataAbertura, dataFechamento, status, valorTotal, observacoes, idVeiculo FROM ordemDeServico WHERE idVeiculo = ?";
        List<OrdemServicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVeiculo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar OS por veículo.", e);
        }
    }

    @Override
    public List<OrdemServicoModel> findAllByStatus(String status) {
        String sql = "SELECT idOS, dataAbertura, dataFechamento, status, valorTotal, observacoes, idVeiculo FROM ordemDeServico WHERE status = CAST(? AS status_os)";
        List<OrdemServicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toLowerCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar OS por status.", e);
        }
    }

    @Override
    public OrdemServicoModel save(OrdemServicoModel os) {
        if (os.getId() == null) {
            String sql = "INSERT INTO ordemDeServico (dataAbertura, dataFechamento, status, valorTotal, observacoes, idVeiculo) VALUES (?,?,CAST(? AS status_os),?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, os.getDataAbertura());
                ps.setObject(2, os.getDataFechamento());
                ps.setString(3, os.getStatus().name().toLowerCase());
                ps.setDouble(4, os.getValorTotal());
                ps.setString(5, os.getObservacoes());
                ps.setInt(6, os.getIdVeiculo());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) os.setId(keys.getInt(1));
                return os;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "ordem de serviço");
                return null;
            }
        } else {
            String sql = "UPDATE ordemDeServico SET dataAbertura=?, dataFechamento=?, status=CAST(? AS status_os), valorTotal=?, observacoes=?, idVeiculo=? WHERE idOS=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, os.getDataAbertura());
                ps.setObject(2, os.getDataFechamento());
                ps.setString(3, os.getStatus().name().toLowerCase());
                ps.setDouble(4, os.getValorTotal());
                ps.setString(5, os.getObservacoes());
                ps.setInt(6, os.getIdVeiculo());
                ps.setInt(7, os.getId());
                ps.executeUpdate();
                return os;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "ordem de serviço");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM ordemDeServico WHERE idOS = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar ordem de serviço.", e);
        }
    }
}