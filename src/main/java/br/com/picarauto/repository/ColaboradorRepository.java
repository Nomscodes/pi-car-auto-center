/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ColaboradorRepository implements IColaboradorRepository {

    // Mapeamento ResultSet → ColaboradorModel
    private ColaboradorModel mapRow(ResultSet rs) throws SQLException {
        ColaboradorModel c = new ColaboradorModel();
        c.setId(rs.getInt("idColaborador"));
        c.setAtivo(true);
        c.setNomeCompleto(rs.getString("nomeCompleto"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setEndereco(rs.getString("endereco"));
        c.setDataAdmissao(rs.getDate("dataAdmissao").toLocalDate());
        c.setSalario(rs.getDouble("salario"));
        c.setIdFuncao(rs.getInt("idFuncao"));
        return c;
    }

    // Consultas genéricas
    @Override
    public ColaboradorModel findByIdAndAtivoTrue(Integer id) {
        String sql = """
                SELECT co.idColaborador, pe.nomeCompleto, pe.telefone, pe.email,
                       pe.endereco, co.dataAdmissao, co.salario, co.idFuncao
                FROM colaborador co
                JOIN pessoa pe ON pe.idPessoa = co.idPessoa
                WHERE co.idColaborador = ?
                """;
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar colaborador.", e);
        }
    }

    @Override
    public List<ColaboradorModel> findAllByAtivoTrue() {
        String sql = """
                SELECT co.idColaborador, pe.nomeCompleto, pe.telefone, pe.email,
                       pe.endereco, co.dataAdmissao, co.salario, co.idFuncao
                FROM colaborador co
                JOIN pessoa pe ON pe.idPessoa = co.idPessoa
                """;
        List<ColaboradorModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar colaboradores.", e);
        }
    }

    // Persistência (INSERT / UPDATE)
    @Override
    public ColaboradorModel save(ColaboradorModel c) {
        Connection conn = ConexaoBanco.getConexao();
        try {
            conn.setAutoCommit(false);

            if (c.getId() == null) {
                // INSERT pessoa
                String sqlPessoa = "INSERT INTO pessoa (nomeCompleto, telefone, email, endereco) VALUES (?,?,?,?)";
                int idPessoa;
                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, c.getNomeCompleto());
                    ps.setString(2, c.getTelefone());
                    ps.setString(3, c.getEmail());
                    ps.setString(4, c.getEndereco());
                    ps.executeUpdate();
                    idPessoa = ps.getGeneratedKeys().getInt(1);
                }

                // INSERT colaborador
                String sqlColab = "INSERT INTO colaborador (dataAdmissao, salario, idFuncao, idPessoa) VALUES (?,?,?,?)";
                int idColaborador;
                try (PreparedStatement ps = conn.prepareStatement(sqlColab, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, Date.valueOf(c.getDataAdmissao()));
                    ps.setDouble(2, c.getSalario());
                    ps.setInt(3, c.getIdFuncao());
                    ps.setInt(4, idPessoa);
                    ps.executeUpdate();
                    idColaborador = ps.getGeneratedKeys().getInt(1);
                }

                c.setId(idColaborador);

            } else {
                // UPDATE pessoa
                String sqlPessoa = """
                        UPDATE pessoa SET nomeCompleto=?, telefone=?, email=?, endereco=?
                        WHERE idPessoa = (SELECT idPessoa FROM colaborador WHERE idColaborador = ?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sqlPessoa)) {
                    ps.setString(1, c.getNomeCompleto());
                    ps.setString(2, c.getTelefone());
                    ps.setString(3, c.getEmail());
                    ps.setString(4, c.getEndereco());
                    ps.setInt(5, c.getId());
                    ps.executeUpdate();
                }

                // UPDATE colaborador
                String sqlColab = "UPDATE colaborador SET dataAdmissao=?, salario=?, idFuncao=? WHERE idColaborador=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlColab)) {
                    ps.setDate(1, Date.valueOf(c.getDataAdmissao()));
                    ps.setDouble(2, c.getSalario());
                    ps.setInt(3, c.getIdFuncao());
                    ps.setInt(4, c.getId());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return c;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
            BusinessException.handleSQLException(e, "colaborador");
            return null;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignored) {
            }
        }
    }

    // Verificações de existência
    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM colaborador WHERE idColaborador = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar colaborador.", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM pessoa WHERE email = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar email do colaborador.", e);
        }
    }
}
