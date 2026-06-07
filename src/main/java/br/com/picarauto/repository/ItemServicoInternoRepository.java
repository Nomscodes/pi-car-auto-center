//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.ItemServicoInternoModel;
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
//public class ItemServicoInternoRepository implements IItemServicoInternoRepository {
// 
//    private ItemServicoInternoModel mapRow(ResultSet rs) throws SQLException {
//        ItemServicoInternoModel i = new ItemServicoInternoModel();
//        i.setId(rs.getInt("idItemServicoInterno"));
//        i.setAtivo(true);
//        i.setValorItem(rs.getDouble("valorItem"));
//        i.setGarantia(rs.getInt("garantia"));
//        i.setObservacoes(rs.getString("observacoes"));
//        i.setIdOS(rs.getInt("idOS"));
//        return i;
//    }
// 
//    @Override
//    public ItemServicoInternoModel findByIdAndAtivoTrue(Integer id) {
//        String sql = "SELECT idItemServicoInterno, valorItem, garantia, observacoes, idOS FROM itemServicoInterno WHERE idItemServicoInterno = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar item de serviço interno.", e);
//        }
//    }
// 
//    @Override
//    public List<ItemServicoInternoModel> findAllByAtivoTrue() {
//        String sql = "SELECT idItemServicoInterno, valorItem, garantia, observacoes, idOS FROM itemServicoInterno";
//        List<ItemServicoInternoModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar itens de serviço interno.", e);
//        }
//    }
// 
//    @Override
//    public List<ItemServicoInternoModel> findAllByIdOS(Integer idOS) {
//        String sql = "SELECT idItemServicoInterno, valorItem, garantia, observacoes, idOS FROM itemServicoInterno WHERE idOS = ?";
//        List<ItemServicoInternoModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idOS);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar itens de serviço interno por OS.", e);
//        }
//    }
// 
//    @Override
//    public ItemServicoInternoModel save(ItemServicoInternoModel i) {
//        if (i.getId() == null) {
//            String sql = "INSERT INTO itemServicoInterno (valorItem, garantia, observacoes, idOS) VALUES (?,?,?,?)";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setDouble(1, i.getValorItem());
//                ps.setInt(2, i.getGarantia());
//                ps.setString(3, i.getObservacoes());
//                ps.setInt(4, i.getIdOS());
//                ps.executeUpdate();
//                ResultSet keys = ps.getGeneratedKeys();
//                if (keys.next()) i.setId(keys.getInt(1));
//                return i;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "item de serviço interno");
//                return null;
//            }
//        } else {
//            String sql = "UPDATE itemServicoInterno SET valorItem=?, garantia=?, observacoes=?, idOS=? WHERE idItemServicoInterno=?";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setDouble(1, i.getValorItem());
//                ps.setInt(2, i.getGarantia());
//                ps.setString(3, i.getObservacoes());
//                ps.setInt(4, i.getIdOS());
//                ps.setInt(5, i.getId());
//                ps.executeUpdate();
//                return i;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "item de serviço interno");
//                return null;
//            }
//        }
//    }
// 
//    @Override
//    public boolean existsById(Integer id) {
//        String sql = "SELECT 1 FROM itemServicoInterno WHERE idItemServicoInterno = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar item de serviço interno.", e);
//        }
//    }
//}