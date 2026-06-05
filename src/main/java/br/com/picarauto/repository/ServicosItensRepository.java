package br.com.picarauto.repository;

import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository para a tabela de relacionamento {@code servicosItens}.
 *
 * Vincula um item de serviço interno ({@code itemServicoInterno})
 * ao seu respectivo serviço do catálogo ({@code servicosInternos}).
 *
 * Chave composta: idServicoInterno + idItemServicoInterno
 *
 * @author Caio4breu
 */
public class ServicosItensRepository implements IServicosItensRepository {

    @Override
    public void save(Integer idServicoInterno, Integer idItemServicoInterno, LocalDate dataExecucao) {
        String sql = "INSERT INTO servicosItens (idServicoInterno, idItemServicoInterno, dataExecucao) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicoInterno);
            ps.setInt(2, idItemServicoInterno);
            ps.setDate(3, Date.valueOf(dataExecucao));
            ps.executeUpdate();
        } catch (SQLException e) {
            BusinessException.handleSQLException(e, "vínculo serviço-item");
        }
    }

    @Override
    public List<ItemServicoInternoModel> findAllByIdServicoInterno(Integer idServicoInterno) {
        // Retorna os itens de OS vinculados a um serviço do catálogo
        String sql = """
                SELECT i.* FROM itemServicoInterno i
                JOIN servicosItens si ON si.idItemServicoInterno = i.idItemServicoInterno
                WHERE si.idServicoInterno = ?
                ORDER BY i.idItemServicoInterno ASC
                """;
        List<ItemServicoInternoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicoInterno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ItemServicoInternoModel item = new ItemServicoInternoModel();
                item.setId(rs.getInt("idItemServicoInterno"));
                item.setAtivo(true);
                item.setValorItem(rs.getDouble("valorItem"));
                item.setGarantia(rs.getInt("garantia"));
                item.setObservacoes(rs.getString("observacoes"));
                item.setIdOS(rs.getInt("idOS"));
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar itens por serviço interno.", e);
        }
    }

    @Override
    public List<Integer> findIdServicoInternoByIdItemServicoInterno(Integer idItemServicoInterno) {
        String sql = "SELECT idServicoInterno FROM servicosItens WHERE idItemServicoInterno = ?";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItemServicoInterno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getInt("idServicoInterno"));
            return ids;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar serviços por item.", e);
        }
    }

    @Override
    public boolean existsByServicoInternoAndItemServicoInterno(Integer idServicoInterno, Integer idItemServicoInterno) {
        String sql = "SELECT 1 FROM servicosItens WHERE idServicoInterno = ? AND idItemServicoInterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicoInterno);
            ps.setInt(2, idItemServicoInterno);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar vínculo serviço-item.", e);
        }
    }

    @Override
    public void delete(Integer idServicoInterno, Integer idItemServicoInterno) {
        String sql = "DELETE FROM servicosItens WHERE idServicoInterno = ? AND idItemServicoInterno = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicoInterno);
            ps.setInt(2, idItemServicoInterno);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao remover vínculo serviço-item.", e);
        }
    }
}