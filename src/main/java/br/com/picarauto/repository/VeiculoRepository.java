//package br.com.picarauto.repository;
//
///**
// *
// * @author Caio4breu
// */
//import br.com.picarauto.model.VeiculoModel;
//import br.com.picarauto.model.exception.BusinessException;
//import br.com.picarauto.util.ConexaoBanco;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class VeiculoRepository implements IVeiculoRepository {
//
//    private VeiculoModel mapRow(ResultSet rs) throws SQLException {
//        VeiculoModel v = new VeiculoModel();
//        v.setId(rs.getInt("idVeiculo"));
//        v.setAtivo(true); // veiculo não tem coluna ativo no schema; sempre ativo quando presente
//        v.setPlaca(rs.getString("placa"));
//        v.setCor(rs.getString("cor"));
//        v.setChassi(rs.getString("chassi"));
//        v.setIdModelo(rs.getInt("idModelo"));
//        v.setIdCliente(rs.getInt("idCliente"));
//        return v;
//    }
//
//    @Override
//    public VeiculoModel findByIdAndAtivoTrue(Integer id) {
//        String sql = "SELECT * FROM veiculo WHERE idVeiculo = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar veículo.", e);
//        }
//    }
//
//    @Override
//    public List<VeiculoModel> findAllByAtivoTrue() {
//        String sql = "SELECT * FROM veiculo ORDER BY idVeiculo ASC";
//        List<VeiculoModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar veículos.", e);
//        }
//    }
//
//    @Override
//    public VeiculoModel save(VeiculoModel v) {
//        if (v.getId() == null) {
//            String sql = "INSERT INTO veiculo (placa, cor, chassi, idModelo, idCliente) VALUES (?,?,?,?,?)";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//                ps.setString(1, v.getPlaca());
//                ps.setString(2, v.getCor());
//                ps.setString(3, v.getChassi());
//                ps.setInt(4, v.getIdModelo());
//                ps.setInt(5, v.getIdCliente());
//                ps.executeUpdate();
//                ResultSet keys = ps.getGeneratedKeys();
//                if (keys.next()) v.setId(keys.getInt(1));
//                return v;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "veículo");
//                return null;
//            }
//        } else {
//            String sql = "UPDATE veiculo SET placa=?, cor=?, chassi=?, idModelo=?, idCliente=? WHERE idVeiculo=?";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setString(1, v.getPlaca());
//                ps.setString(2, v.getCor());
//                ps.setString(3, v.getChassi());
//                ps.setInt(4, v.getIdModelo());
//                ps.setInt(5, v.getIdCliente());
//                ps.setInt(6, v.getId());
//                ps.executeUpdate();
//                return v;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "veículo");
//                return null;
//            }
//        }
//    }
//
//    @Override
//    public boolean existsById(Integer id) {
//        String sql = "SELECT 1 FROM veiculo WHERE idVeiculo = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar veículo.", e);
//        }
//    }
//
//    @Override
//    public boolean existsByPlaca(String placa) {
//        String sql = "SELECT 1 FROM veiculo WHERE placa = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, placa);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar placa.", e);
//        }
//    }
//}