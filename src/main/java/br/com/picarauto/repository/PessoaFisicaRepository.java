package br.com.picarauto.repository;

import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaRepository implements IPessoaFisicaRepository {

    private PessoaFisicaModel mapRow(ResultSet rs) throws SQLException {
        PessoaFisicaModel p = new PessoaFisicaModel();
        p.setId(rs.getInt("idCliente"));
        p.setAtivo(true);
        p.setNomeCompleto(rs.getString("nomeCompleto"));
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        p.setEndereco(rs.getString("endereco"));
        p.setDataCadastro(rs.getDate("dataCadastro"));
        p.setCpf(rs.getString("cpf"));
        p.setRg(rs.getString("rg"));
        p.setDataNascimento(rs.getDate("dataNascimento"));
        return p;
    }

    @Override
    public PessoaFisicaModel findByIdAndAtivoTrue(Integer id) {
        String sql = """
                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
                       pe.endereco, cl.dataCadastro, pf.cpf, pf.rg, pf.dataNascimento
                FROM pessoaFisica pf
                JOIN cliente cl ON cl.idCliente = pf.idCliente
                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
                WHERE cl.idCliente = ?
                """;
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar pessoa física.", e);
        }
    }

    @Override
    public List<PessoaFisicaModel> findAllByAtivoTrue() {
        String sql = """
                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
                       pe.endereco, cl.dataCadastro, pf.cpf, pf.rg, pf.dataNascimento
                FROM pessoaFisica pf
                JOIN cliente cl ON cl.idCliente = pf.idCliente
                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
                """;
        List<PessoaFisicaModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar pessoas físicas.", e);
        }
    }

    @Override
    public PessoaFisicaModel save(PessoaFisicaModel p) {
        Connection conn = ConexaoBanco.getConexao();
        try {
            conn.setAutoCommit(false);

            if (p.getId() == null) {
                // INSERT pessoa
                String sqlPessoa = "INSERT INTO pessoa (nomeCompleto, telefone, email, endereco) VALUES (?,?,?,?)";
                int idPessoa;
                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, p.getNomeCompleto());
                    ps.setString(2, p.getTelefone());
                    ps.setString(3, p.getEmail());
                    ps.setString(4, p.getEndereco());
                    ps.executeUpdate();
                    idPessoa = ps.getGeneratedKeys().getInt(1);
                }

                // INSERT cliente
                String sqlCliente = "INSERT INTO cliente (dataCadastro, idPessoa) VALUES (?,?)";
                int idCliente;
                try (PreparedStatement ps = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, new java.sql.Date(p.getDataCadastro().getTime()));
                    ps.setInt(2, idPessoa);
                    ps.executeUpdate();
                    idCliente = ps.getGeneratedKeys().getInt(1);
                }

                // INSERT pessoaFisica
                String sqlPf = "INSERT INTO pessoaFisica (cpf, rg, dataNascimento, idCliente) VALUES (?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlPf)) {
                    ps.setString(1, p.getCpf());
                    ps.setString(2, p.getRg());
                    ps.setDate(3, new java.sql.Date(p.getDataNascimento().getTime()));
                    ps.setInt(4, idCliente);
                    ps.executeUpdate();
                }

                p.setId(idCliente);

            } else {
                // UPDATE pessoa
                String sqlPessoa = """
                        UPDATE pessoa SET nomeCompleto=?, telefone=?, email=?, endereco=?
                        WHERE idPessoa = (SELECT idPessoa FROM cliente WHERE idCliente = ?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa)) {
                    ps.setString(1, p.getNomeCompleto());
                    ps.setString(2, p.getTelefone());
                    ps.setString(3, p.getEmail());
                    ps.setString(4, p.getEndereco());
                    ps.setInt(5, p.getId());
                    ps.executeUpdate();
                }

                // UPDATE pessoaFisica
                String sqlPf = "UPDATE pessoaFisica SET rg=?, dataNascimento=? WHERE cpf=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlPf)) {
                    ps.setString(1, p.getRg());
                    ps.setDate(2, new java.sql.Date(p.getDataNascimento().getTime()));
                    ps.setString(3, p.getCpf());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return p;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            BusinessException.handleSQLException(e, "pessoa física");
            return null;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM pessoaFisica pf JOIN cliente cl ON cl.idCliente = pf.idCliente WHERE cl.idCliente = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar pessoa física.", e);
        }
    }

    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT 1 FROM pessoaFisica WHERE cpf = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar CPF.", e);
        }
    }

    @Override
    public PessoaFisicaModel findByCpf(String cpf) {
        String sql = """
                SELECT pe.idPessoa AS idCliente, pe.nomeCompleto, pe.telefone, pe.email,
                       pe.endereco, cl.dataCadastro, pf.cpf, pf.rg, pf.dataNascimento
                FROM pessoaFisica pf
                JOIN cliente cl ON cl.idCliente = pf.idCliente
                JOIN pessoa pe  ON pe.idPessoa  = cl.idPessoa
                WHERE pf.cpf = ?
                """;
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar pessoa física por CPF.", e);
        }
    }
}