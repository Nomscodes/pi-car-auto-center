package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository implements IClienteRepository {

    private ClienteModel mapRow(ResultSet rs) throws SQLException {
        ClienteModel c = new ClienteModel();
        c.setId(rs.getInt("id"));
        c.setAtivo(rs.getBoolean("ativo"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setEmail(rs.getString("email"));
        c.setTelefone(rs.getString("telefone"));
        c.setEndereco(rs.getString("endereco"));
        c.setBairro(rs.getString("bairro"));
        c.setCidade(rs.getString("cidade"));
        c.setEstado(rs.getString("estado"));
        c.setCep(rs.getString("cep"));
        return c;
    }

    @Override
    public ClienteModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM cliente WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar cliente.", e);
        }
    }

    @Override
    public List<ClienteModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM cliente WHERE ativo = 1";
        List<ClienteModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar clientes.", e);
        }
    }

    @Override
    public ClienteModel save(ClienteModel c) {
        if (c.getId() == null) {
            String sql = "INSERT INTO cliente (nome, cpf, email, telefone, endereco, bairro, cidade, estado, cep, ativo, data_hora_criacao) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getNome());
                ps.setString(2, c.getCpf());
                ps.setString(3, c.getEmail());
                ps.setString(4, c.getTelefone());
                ps.setString(5, c.getEndereco());
                ps.setString(6, c.getBairro());
                ps.setString(7, c.getCidade());
                ps.setString(8, c.getEstado());
                ps.setString(9, c.getCep());
                ps.setBoolean(10, c.isAtivo());
                ps.setTimestamp(11, new Timestamp(c.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) c.setId(keys.getInt(1));
                return c;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "cliente");
                return null;
            }
        } else {
            String sql = "UPDATE cliente SET nome=?, cpf=?, email=?, telefone=?, endereco=?, bairro=?, cidade=?, estado=?, cep=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getNome());
                ps.setString(2, c.getCpf());
                ps.setString(3, c.getEmail());
                ps.setString(4, c.getTelefone());
                ps.setString(5, c.getEndereco());
                ps.setString(6, c.getBairro());
                ps.setString(7, c.getCidade());
                ps.setString(8, c.getEstado());
                ps.setString(9, c.getCep());
                ps.setBoolean(10, c.isAtivo());
                ps.setInt(11, c.getId());
                ps.executeUpdate();
                return c;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "cliente");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM cliente WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar cliente.", e);
        }
    }

    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT 1 FROM cliente WHERE cpf = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar CPF.", e);
        }
    }
}