package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoRepository implements IVeiculoRepository {

    private VeiculoModel mapRow(ResultSet rs) throws SQLException {
        VeiculoModel v = new VeiculoModel();
        v.setId(rs.getInt("id"));
        v.setAtivo(rs.getBoolean("ativo"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnoFabricacao(rs.getInt("ano_fabricacao"));
        v.setCor(rs.getString("cor"));
        v.setQuilometragem(rs.getInt("quilometragem"));
        int clienteId = rs.getInt("cliente_id");
        if (!rs.wasNull()) {
            ClienteModel cliente = new ClienteModel();
            cliente.setId(clienteId);
            v.setCliente(cliente);
        }
        return v;
    }

    @Override
    public VeiculoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM veiculo WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar veículo.", e);
        }
    }

    @Override
    public List<VeiculoModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM veiculo WHERE ativo = 1";
        List<VeiculoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar veículos.", e);
        }
    }

    @Override
    public VeiculoModel save(VeiculoModel v) {
        if (v.getId() == null) {
            String sql = "INSERT INTO veiculo (placa, marca, modelo, ano_fabricacao, cor, quilometragem, cliente_id, ativo, data_hora_criacao) VALUES (?,?,?,?,?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, v.getPlaca());
                ps.setString(2, v.getMarca());
                ps.setString(3, v.getModelo());
                ps.setInt(4, v.getAnoFabricacao());
                ps.setString(5, v.getCor());
                ps.setObject(6, v.getQuilometragem());
                ps.setObject(7, v.getCliente() != null ? v.getCliente().getId() : null);
                ps.setBoolean(8, v.isAtivo());
                ps.setTimestamp(9, new Timestamp(v.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) v.setId(keys.getInt(1));
                return v;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "veículo");
                return null;
            }
        } else {
            String sql = "UPDATE veiculo SET placa=?, marca=?, modelo=?, ano_fabricacao=?, cor=?, quilometragem=?, cliente_id=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, v.getPlaca());
                ps.setString(2, v.getMarca());
                ps.setString(3, v.getModelo());
                ps.setInt(4, v.getAnoFabricacao());
                ps.setString(5, v.getCor());
                ps.setObject(6, v.getQuilometragem());
                ps.setObject(7, v.getCliente() != null ? v.getCliente().getId() : null);
                ps.setBoolean(8, v.isAtivo());
                ps.setInt(9, v.getId());
                ps.executeUpdate();
                return v;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "veículo");
                return null;
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM veiculo WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar veículo.", e);
        }
    }

    @Override
    public boolean existsByPlaca(String placa) {
        String sql = "SELECT 1 FROM veiculo WHERE placa = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar placa.", e);
        }
    }
}