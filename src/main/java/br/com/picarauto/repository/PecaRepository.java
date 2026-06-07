//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.PecaModel;
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
//public class PecaRepository implements IPecaRepository {
//
//    private PecaModel mapRow(ResultSet rs) throws SQLException {
//        PecaModel p = new PecaModel();
//        p.setCodigoNacional(rs.getInt("codigoNacional"));
//        p.setAtivo(rs.getBoolean("ativo"));
//        p.setModelo(rs.getString("modelo"));
//        p.setMarca(rs.getString("marca"));
//        p.setAnoVeiculo(rs.getInt("anoVeiculo"));
//        p.setAnoModelo(rs.getInt("anoModelo"));
//        p.setPrecoUnitario(rs.getDouble("precoUnitario"));
//        p.setGarantia(rs.getInt("garantia"));
//        p.setIdFornecedor(rs.getInt("idFornecedor"));
//        return p;
//    }
//
//    // ─── IGenericRepository — métodos por id herdado (não usados pela lógica de negócio) ───
//
//    @Override
//    public PecaModel findByIdAndAtivoTrue(Integer id) {
//        throw new UnsupportedOperationException("Peça usa codigoNacional como chave. Use findByCodigoNacional().");
//    }
//
//    @Override
//    public boolean existsById(Integer id) {
//        throw new UnsupportedOperationException("Peça usa codigoNacional como chave. Use existsByCodigoNacional().");
//    }
//
//    // ─── Consultas por codigoNacional ─────────────────────────────────────────
//
//    @Override
//    public PecaModel findByCodigoNacional(Integer codigoNacional) {
//        String sql = "SELECT * FROM peca WHERE codigoNacional = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, codigoNacional);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar peça.", e);
//        }
//    }
//
//    @Override
//    public List<PecaModel> findAllByAtivoTrue() {
//        String sql = "SELECT * FROM peca ORDER BY codigoNacional ASC";
//        List<PecaModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar peças.", e);
//        }
//    }
//
//    // ─── Persistência ─────────────────────────────────────────────────────────
//
//    @Override
//    public PecaModel save(PecaModel p) {
//        if (!existsByCodigoNacional(p.getCodigoNacional())) {
//            String sql = "INSERT INTO peca (codigoNacional, modelo, marca, anoVeiculo, anoModelo, precoUnitario, garantia, idFornecedor) "
//                       + "VALUES (?,?,?,?,?,?,?,?)";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setInt(1, p.getCodigoNacional());
//                ps.setString(2, p.getModelo());
//                ps.setString(3, p.getMarca());
//                ps.setInt(4, p.getAnoVeiculo());
//                ps.setInt(5, p.getAnoModelo());
//                ps.setDouble(6, p.getPrecoUnitario());
//                ps.setInt(7, p.getGarantia());
//                ps.setInt(8, p.getIdFornecedor());
//                ps.executeUpdate();
//                return p;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "peça");
//                return null;
//            }
//        } else {
//            String sql = "UPDATE peca SET modelo=?, marca=?, anoVeiculo=?, anoModelo=?, precoUnitario=?, garantia=?, idFornecedor=? "
//                       + "WHERE codigoNacional=?";
//            try (Connection conn = ConexaoBanco.getConexao();
//                 PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setString(1, p.getModelo());
//                ps.setString(2, p.getMarca());
//                ps.setInt(3, p.getAnoVeiculo());
//                ps.setInt(4, p.getAnoModelo());
//                ps.setDouble(5, p.getPrecoUnitario());
//                ps.setInt(6, p.getGarantia());
//                ps.setInt(7, p.getIdFornecedor());
//                ps.setInt(8, p.getCodigoNacional());
//                ps.executeUpdate();
//                return p;
//            } catch (SQLException e) {
//                BusinessException.handleSQLException(e, "peça");
//                return null;
//            }
//        }
//    }
//
//    @Override
//    public boolean existsByCodigoNacional(Integer codigoNacional) {
//        String sql = "SELECT 1 FROM peca WHERE codigoNacional = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, codigoNacional);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar código nacional.", e);
//        }
//    }
//}