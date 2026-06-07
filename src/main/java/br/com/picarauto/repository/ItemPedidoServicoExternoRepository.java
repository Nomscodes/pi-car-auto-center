//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.ItemPedidoServicoExternoModel;
//import br.com.picarauto.model.exception.BusinessException;
//import br.com.picarauto.util.ConexaoBanco;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author Caio4breu
// */
//public class ItemPedidoServicoExternoRepository implements IItemPedidoServicoExternoRepository {
//
//    private ItemPedidoServicoExternoModel mapRow(ResultSet rs) throws SQLException {
//        ItemPedidoServicoExternoModel i = new ItemPedidoServicoExternoModel();
//        i.setId(rs.getInt("idItemPedidoServicoExterno"));
//        i.setAtivo(true);
//        i.setValorItem(rs.getDouble("valorItem"));
//        i.setGarantia(rs.getInt("garantia"));
//        i.setObservacoes(rs.getString("observacoes"));
//        i.setIdServicoExterno(rs.getInt("idServicoExterno"));
//        return i;
//    }
//
//    @Override
//    public ItemPedidoServicoExternoModel findByIdAndAtivoTrue(Integer id) {
//        String sql = "SELECT idItemPedidoServicoExterno, valorItem, garantia, observacoes, idServicoExterno FROM itemPedidoServicoExterno WHERE idItemPedidoServicoExterno = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar item de serviço externo.", e);
//        }
//    }
//
//    @Override
//    public List<ItemPedidoServicoExternoModel> findAllByAtivoTrue() {
//        String sql = "SELECT idItemPedidoServicoExterno, valorItem, garantia, observacoes, idServicoExterno FROM itemPedidoServicoExterno";
//        List<ItemPedidoServicoExternoModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar itens de serviço externo.", e);
//        }
//    }
//
//    @Override
//    public List<ItemPedidoServicoExternoModel> findAllByIdServicoExterno(Integer idServicoExterno) {
//        String sql = "SELECT idItemPedidoServicoExterno, descricao, valorCobrado, garantia, observacoes, idServicoExterno FROM itemPedidoServicoExterno WHERE idServicoExterno = ?";
//        List<ItemPedidoServicoExternoModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idServicoExterno);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar itens de serviço externo por serviço.", e);
//        }
//    }
//
//    @Override
//    public ItemPedidoServicoExternoModel save(ItemPedidoServicoExternoModel i) {
//        if (i.getId() == null) {
//            String sql = "INSERT INTO itemPedidoServicoExterno (valorItem, garantia, observacoes, idServicoExterno) VALUES (?,?,?,?)";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setDouble(1, i.getValorItem());
//                ps.setInt(2, i.getGarantia());
//                ps.setString(3, i.getObservacoes());
//                ps.setInt(4, i.getIdServicoExterno());
//                ps.executeUpdate();
//                ResultSet keys = ps.getGeneratedKeys();
//                if (keys.next()) i.setId(keys.getInt(1));
//                return i;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "item de serviço externo");
//                return null;
//            }
//        } else {
//            String sql = "UPDATE itemPedidoServicoExterno SET valorItem=?, garantia=?, observacoes=?, idServicoExterno=? WHERE idItemPedidoServicoExterno=?";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setDouble(1, i.getValorItem());
//                ps.setInt(2, i.getGarantia());
//                ps.setString(3, i.getObservacoes());
//                ps.setInt(4, i.getIdServicoExterno());
//                ps.setInt(5, i.getId());
//                ps.executeUpdate();
//                return i;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "item de serviço externo");
//                return null;
//            }
//        }
//    }
//
//    @Override
//    public boolean existsById(Integer id) {
//        String sql = "SELECT 1 FROM itemPedidoServicoExterno WHERE idItemPedidoServicoExterno = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar item de serviço externo.", e);
//        }
//    }
//}