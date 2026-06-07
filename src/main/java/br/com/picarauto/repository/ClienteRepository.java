//package br.com.picarauto.repository;
//
//import br.com.picarauto.model.ClienteModel;
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
//public class ClienteRepository implements IClienteRepository {
//
//    private ClienteModel mapRow(ResultSet rs) throws SQLException {
//        ClienteModel c = new ClienteModel();
//        c.setId(rs.getInt("idCliente"));
//        c.setAtivo(true);
//        c.setNomeCompleto(rs.getString("nomeCompleto"));
//        c.setTelefone(rs.getString("telefone"));
//        c.setEmail(rs.getString("email"));
//        c.setEndereco(rs.getString("endereco"));
//        c.setDataCadastro(rs.getDate("dataCadastro"));
//        return c;
//    }
//
//    @Override
//    public ClienteModel findByIdAndAtivoTrue(Integer id) {
//        String sql = """
//                SELECT cl.idCliente, cl.dataCadastro,
//                       pe.nomeCompleto, pe.telefone, pe.email, pe.endereco
//                FROM cliente cl
//                JOIN pessoa pe ON pe.idPessoa = cl.idPessoa
//                WHERE cl.idCliente = ?
//                """;
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) return mapRow(rs);
//            return null;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao buscar cliente.", e);
//        }
//    }
//
//    @Override
//    public List<ClienteModel> findAllByAtivoTrue() {
//        String sql = """
//                SELECT cl.idCliente, cl.dataCadastro,
//                       pe.nomeCompleto, pe.telefone, pe.email, pe.endereco
//                FROM cliente cl
//                JOIN pessoa pe ON pe.idPessoa = cl.idPessoa
//                """;
//        List<ClienteModel> list = new ArrayList<>();
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) list.add(mapRow(rs));
//            return list;
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao listar clientes.", e);
//        }
//    }
//
//    @Override
//    public ClienteModel save(ClienteModel c) {
//        Connection conn = ConexaoBanco.getConexao();
//        try {
//            conn.setAutoCommit(false);
//
//            if (c.getId() == null) {
//                String sqlPessoa = "INSERT INTO pessoa (nomeCompleto, telefone, email, endereco) VALUES (?,?,?,?)";
//                int idPessoa;
//                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setString(1, c.getNomeCompleto());
//                    ps.setString(2, c.getTelefone());
//                    ps.setString(3, c.getEmail());
//                    ps.setString(4, c.getEndereco());
//                    ps.executeUpdate();
//                    idPessoa = ps.getGeneratedKeys().getInt(1);
//                }
//
//                String sqlCliente = "INSERT INTO cliente (dataCadastro, idPessoa) VALUES (?,?)";
//                try (PreparedStatement ps = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setDate(1, new java.sql.Date(c.getDataCadastro().getTime()));
//                    ps.setInt(2, idPessoa);
//                    ps.executeUpdate();
//                    c.setId(ps.getGeneratedKeys().getInt(1));
//                }
//
//            } else {
//                String sqlPessoa = """
//                        UPDATE pessoa SET nomeCompleto=?, telefone=?, email=?, endereco=?
//                        WHERE idPessoa = (SELECT idPessoa FROM cliente WHERE idCliente = ?)
//                        """;
//                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa)) {
//                    ps.setString(1, c.getNomeCompleto());
//                    ps.setString(2, c.getTelefone());
//                    ps.setString(3, c.getEmail());
//                    ps.setString(4, c.getEndereco());
//                    ps.setInt(5, c.getId());
//                    ps.executeUpdate();
//                }
//            }
//
//            conn.commit();
//            return c;
//
//        } catch (SQLException e) {
//            try { conn.rollback(); } catch (SQLException ignored) {}
//            BusinessException.handleSQLException(e, "cliente");
//            return null;
//        } finally {
//            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
//        }
//    }
//
//    @Override
//    public boolean existsById(Integer id) {
//        String sql = "SELECT 1 FROM cliente WHERE idCliente = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar cliente.", e);
//        }
//    }
//
//    @Override
//    public boolean existsByCpf(String cpf) {
//        String sql = "SELECT 1 FROM pessoaFisica WHERE cpf = ?";
//        try (Connection conn = ConexaoBanco.getConexao();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, cpf);
//            return ps.executeQuery().next();
//        } catch (SQLException e) {
//            throw new BusinessException("Erro ao verificar CPF.", e);
//        }
//    }
//}