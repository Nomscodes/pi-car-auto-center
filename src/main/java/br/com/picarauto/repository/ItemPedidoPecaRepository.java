package br.com.picarauto.repository;

import br.com.picarauto.model.ItemPedidoPecaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Caio4breu
 */
public class ItemPedidoPecaRepository implements IItemPedidoPecaRepository {

    private ItemPedidoPecaModel mapRow(ResultSet rs) throws SQLException {
        ItemPedidoPecaModel i = new ItemPedidoPecaModel();
        i.setId(rs.getInt("idItemPedidoPeca"));
        i.setAtivo(true);
        i.setQuantidade(rs.getInt("quantidade"));
        i.setDataEntrega(rs.getDate("dataEntrega"));
        i.setCodigoNacional(rs.getInt("codigoNacional"));
        i.setIdFornecedor(rs.getInt("idFornecedor"));
        i.setIdOS(rs.getInt("idOS"));
        return i;
    }

    @Override
    public ItemPedidoPecaModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT idItemPedidoPeca, quantidade, dataEntrega, codigoNacional, idFornecedor, idOS FROM itemPedidoPeca WHERE idItemPedidoPeca = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar item de pedido de peça.", e);
        }
    }

    @Override
    public List<ItemPedidoPecaModel> findAllByAtivoTrue() {
        String sql = "SELECT idItemPedidoPeca, quantidade, dataEntrega, codigoNacional, idFornecedor, idOS FROM itemPedidoPeca";
        List<ItemPedidoPecaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar itens de pedido de peça.", e);
        }
    }

    @Override
    public List<ItemPedidoPecaModel> findAllByIdOS(Integer idOS) {
        String sql = "SELECT idItemPedidoPeca, quantidade, dataEntrega, codigoNacional, idFornecedor, idOS FROM itemPedidoPeca WHERE idOS = ?";
        List<ItemPedidoPecaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOS);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar itens de pedido de peça por OS.", e);
        }
    }

    @Override
    public List<ItemPedidoPecaModel> findAllByCodigoNacional(Integer codigoNacional) {
        String sql = "SELECT idItemPedidoPeca, quantidade, dataEntrega, codigoNacional, idFornecedor, idOS FROM itemPedidoPeca WHERE codigoNacional = ?";
        List<ItemPedidoPecaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigoNacional);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar itens de pedido de peça por código nacional.", e);
        }
    }

    @Override
    public ItemPedidoPecaModel save(ItemPedidoPecaModel i) {
        if (i.getId() == null) {
            String sql = "INSERT INTO itemPedidoPeca (quantidade, dataEntrega, codigoNacional, idFornecedor, idOS) VALUES (?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, i.getQuantidade());
                ps.setDate(2, i.getDataEntrega() != null ? new java.sql.Date(i.getDataEntrega().getTime()) : null);
                ps.setInt(3, i.getCodigoNacional());
                ps.setInt(4, i.getIdFornecedor());
                ps.setInt(5, i.getIdOS());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) i.setId(keys.getInt(1));
                return i;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "item de pedido de peça");
                return null;
            }
        } else {
            String sql = "UPDATE itemPedidoPeca SET quantidade=?, dataEntrega=?, codigoNacional=?, idFornecedor=?, idOS=? WHERE idItemPedidoPeca=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, i.getQuantidade());
                ps.setDate(2, i.getDataEntrega() != null ? new java.sql.Date(i.getDataEntrega().getTime()) : null);
                ps.setInt(3, i.getCodigoNacional());
                ps.setInt(4, i.getIdFornecedor());
                ps.setInt(5, i.getIdOS());
                ps.setInt(6, i.getId());
                ps.executeUpdate();
                return i;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "item de pedido de peça");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM itemPedidoPeca WHERE idItemPedidoPeca = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar item de pedido de peça.", e);
        }
    }
}