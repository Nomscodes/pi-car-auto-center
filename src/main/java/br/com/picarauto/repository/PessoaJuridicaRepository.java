//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.PessoaJuridicaModel;
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
//public class PessoaJuridicaRepository implements IPessoaJuridicaRepository {
//
//    private PessoaJuridicaModel mapRow(ResultSet rs) throws SQLException {
//        PessoaJuridicaModel p = new PessoaJuridicaModel();
//        p.setId(rs.getInt("idCliente"));
//        p.setAtivo(true);
//        p.setNomeCompleto(rs.getString("nomeCompleto"));
//        p.setTelefone(rs.getString("telefone"));
//        p.setEmail(rs.getString("email"));
//        p.setEndereco(rs.getString("endereco"));
//        p.setDataCadastro(rs.getDate("dataCadastro"));
//        p.setCnpj(rs.getString("cnpj"));
//        p.setRazaoSocial(rs.getString("razaoSocial"));
//        p.setNomeFantasia(rs.getString("nomeFantasia"));
//        p.setDataAbertura(rs.getDate("dataAbertura"));
//        return p;
//    }
//
//    @Override
//    public PessoaJuridicaModel findByIdAndAtivoTrue(Integer id) {
//        String sql = """
//                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
//                       pe.endereco, cl.dataCadastro, pj.cnpj, pj.razaoSocial,
//                       pj.nomeFantasia, pj.dataAbertura
//                FROM pessoaJuridica pj
//                JOIN cliente cl ON cl.idCliente = pj.idCliente
//                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
//                WHERE cl.idCliente = ?
//                """;
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar pessoa jurídica.", e);
//        }
//    }
//
//    @Override
//    public List<PessoaJuridicaModel> findAllByAtivoTrue() {
//        String sql = """
//                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
//                       pe.endereco, cl.dataCadastro, pj.cnpj, pj.razaoSocial,
//                       pj.nomeFantasia, pj.dataAbertura
//                FROM pessoaJuridica pj
//                JOIN cliente cl ON cl.idCliente = pj.idCliente
//                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
//                """;
//        List<PessoaJuridicaModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar pessoas jurídicas.", e);
//        }
//    }
//
//    @Override
//    public PessoaJuridicaModel save(PessoaJuridicaModel p) {
//        Connection conn = ConexaoBanco.getConexao();
//        try {
//            conn.setAutoCommit(false);
//
//            if (p.getId() == null) {
//                // INSERT pessoa
//                String sqlPessoa = "INSERT INTO pessoa (nomeCompleto, telefone, email, endereco) VALUES (?,?,?,?)";
//                int idPessoa;
//                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setString(1, p.getNomeCompleto());
//                    ps.setString(2, p.getTelefone());
//                    ps.setString(3, p.getEmail());
//                    ps.setString(4, p.getEndereco());
//                    ps.executeUpdate();
//                    idPessoa = ps.getGeneratedKeys().getInt(1);
//                }
//
//                // INSERT cliente
//                String sqlCliente = "INSERT INTO cliente (dataCadastro, idPessoa) VALUES (?,?)";
//                int idCliente;
//                try (PreparedStatement ps = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setDate(1, new java.sql.Date(p.getDataCadastro().getTime()));
//                    ps.setInt(2, idPessoa);
//                    ps.executeUpdate();
//                    idCliente = ps.getGeneratedKeys().getInt(1);
//                }
//
//                // INSERT pessoaJuridica
//                String sqlPj = "INSERT INTO pessoaJuridica (cnpj, razaoSocial, nomeFantasia, dataAbertura, idCliente) VALUES (?,?,?,?,?)";
//                try (PreparedStatement ps = conn.prepareStatement(sqlPj)) {
//                    ps.setString(1, p.getCnpj());
//                    ps.setString(2, p.getRazaoSocial());
//                    ps.setString(3, p.getNomeFantasia());
//                    ps.setDate(4, new java.sql.Date(p.getDataAbertura().getTime()));
//                    ps.setInt(5, idCliente);
//                    ps.executeUpdate();
//                }
//
//                p.setId(idCliente);
//
//            } else {
//                // UPDATE pessoa
//                String sqlPessoa = """
//                        UPDATE pessoa SET nomeCompleto=?, telefone=?, email=?, endereco=?
//                        WHERE idPessoa = (SELECT idPessoa FROM cliente WHERE idCliente = ?)
//                        """;
//                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa)) {
//                    ps.setString(1, p.getNomeCompleto());
//                    ps.setString(2, p.getTelefone());
//                    ps.setString(3, p.getEmail());
//                    ps.setString(4, p.getEndereco());
//                    ps.setInt(5, p.getId());
//                    ps.executeUpdate();
//                }
//
//                // UPDATE pessoaJuridica
//                String sqlPj = "UPDATE pessoaJuridica SET razaoSocial=?, nomeFantasia=?, dataAbertura=? WHERE cnpj=?";
//                try (PreparedStatement ps = conn.prepareStatement(sqlPj)) {
//                    ps.setString(1, p.getRazaoSocial());
//                    ps.setString(2, p.getNomeFantasia());
//                    ps.setDate(3, new java.sql.Date(p.getDataAbertura().getTime()));
//                    ps.setString(4, p.getCnpj());
//                    ps.executeUpdate();
//                }
//            }
//
//            conn.commit();
//            return p;
//
//        } catch (SQLException e) {
//            try { conn.rollback(); } catch (SQLException ignored) {}
//            BusinessException.handleSQLException(e, "pessoa jurídica");
//            return null;
//        } finally {
//            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
//        }
//    }
//
//    @Override
//    public boolean existsById(Integer id) {
//        String sql = "SELECT 1 FROM pessoaJuridica pj JOIN cliente cl ON cl.idCliente = pj.idCliente WHERE cl.idCliente = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar pessoa jurídica.", e);
//        }
//    }
//
//    @Override
//    public boolean existsByCnpj(String cnpj) {
//        String sql = "SELECT 1 FROM pessoaJuridica WHERE cnpj = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, cnpj);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar CNPJ.", e);
//        }
//    }
//
//    @Override
//    public PessoaJuridicaModel findByCnpj(String cnpj) {
//        String sql = """
//                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
//                       pe.endereco, cl.dataCadastro, pj.cnpj, pj.razaoSocial,
//                       pj.nomeFantasia, pj.dataAbertura
//                FROM pessoaJuridica pj
//                JOIN cliente cl ON cl.idCliente = pj.idCliente
//                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
//                WHERE pj.cnpj = ?
//                """;
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, cnpj);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar pessoa jurídica por CNPJ.", e);
//        }
//    }
//}