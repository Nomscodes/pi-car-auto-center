//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.exception.BusinessException;
//import br.com.picarauto.util.ConexaoBanco;
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Repository para a tabela de relacionamento {@code itemFornecedor}.
// *
// * Vincula um fornecedor ({@code fornecedor}) ao serviço externo prestado
// * ({@code itemPedidoServicoExterno}), registrando a data de execução.
// *
// * Chave composta: idFornecedor + idItemPedidoServicoExterno + dataExecucao
// *
// * @author Caio4breu
// */
//public class ItemFornecedorRepository implements IItemFornecedorRepository {
//
//    @Override
//    public void save(Integer idFornecedor, Integer idItemPedidoServicoExterno, LocalDate dataExecucao) {
//        String sql = "INSERT INTO itemFornecedor (idFornecedor, idItemPedidoServicoExterno, dataExecucao) VALUES (?, ?, ?)";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idFornecedor);
//            ps.setInt(2, idItemPedidoServicoExterno);
//            ps.setDate(3, Date.valueOf(dataExecucao));
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            BusinessException.handleSQLException(e, "vínculo fornecedor-item");
//        }
//    }
//
//    @Override
//    public List<Integer> findIdItemByIdFornecedor(Integer idFornecedor) {
//        String sql = "SELECT idItemPedidoServicoExterno FROM itemFornecedor WHERE idFornecedor = ?";
//        List<Integer> ids = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idFornecedor);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) ids.add(rs.getInt("idItemPedidoServicoExterno"));
//            return ids;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar itens por fornecedor.", e);
//        }
//    }
//
//    @Override
//    public List<Integer> findIdFornecedorByIdItem(Integer idItemPedidoServicoExterno) {
//        String sql = "SELECT idFornecedor FROM itemFornecedor WHERE idItemPedidoServicoExterno = ?";
//        List<Integer> ids = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idItemPedidoServicoExterno);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) ids.add(rs.getInt("idFornecedor"));
//            return ids;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar fornecedores por item.", e);
//        }
//    }
//
//    @Override
//    public boolean existsByFornecedorAndItem(Integer idFornecedor, Integer idItemPedidoServicoExterno) {
//        String sql = "SELECT 1 FROM itemFornecedor WHERE idFornecedor = ? AND idItemPedidoServicoExterno = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idFornecedor);
//            ps.setInt(2, idItemPedidoServicoExterno);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar vínculo fornecedor-item.", e);
//        }
//    }
//
//    @Override
//    public void delete(Integer idFornecedor, Integer idItemPedidoServicoExterno, LocalDate dataExecucao) {
//        String sql = "DELETE FROM itemFornecedor WHERE idFornecedor = ? AND idItemPedidoServicoExterno = ? AND dataExecucao = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, idFornecedor);
//            ps.setInt(2, idItemPedidoServicoExterno);
//            ps.setDate(3, Date.valueOf(dataExecucao));
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao remover vínculo fornecedor-item.", e);
//        }
//    }
//}