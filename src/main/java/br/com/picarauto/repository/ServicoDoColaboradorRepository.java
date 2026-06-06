/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.picarauto.repository;

import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ServicoDoColaboradorRepository implements IServicoDoColaboradorRepository {

    //Persistência (INSERT)
    @Override
    public void save(Integer idColaborador, Integer idServicoInterno, LocalDate dataServico) {
        String sql = "INSERT INTO servicosDoColaborador (idColaborador, idServicoInterno, dataServico) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ps.setInt(2, idServicoInterno);
            ps.setDate(3, Date.valueOf(dataServico));
            ps.executeUpdate();
        } catch (SQLException e) {
            BusinessException.handleSQLException(e, "serviço do colaborador");
        }
    }

    //Consultas específicas
    @Override
    public List<Integer> findIdServicoInternoByIdColaborador(Integer idColaborador) {
        String sql = "SELECT idServicoInterno FROM servicosDoColaborador WHERE idColaborador = ?";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("idServicoInterno"));
            }
            return ids;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar serviços por colaborador.", e);
        }
    }

    @Override
    public List<Integer> findIdColaboradorByIdServicoInterno(Integer idServicoInterno) {
        String sql = "SELECT idColaborador FROM servicosDoColaborador WHERE idServicoInterno = ?";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idServicoInterno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("idColaborador"));
            }
            return ids;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar colaboradores por serviço.", e);
        }
    }

    //Verificações de existência
    @Override
    public boolean existsByIdColaboradorAndIdServicoInterno(Integer idColaborador, Integer idServicoInterno) {
        String sql = "SELECT 1 FROM servicosDoColaborador WHERE idColaborador = ? AND idServicoInterno = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ps.setInt(2, idServicoInterno);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar serviço do colaborador.", e);
        }
    }

    //Remoção (DELETE)
    @Override
    public void delete(Integer idColaborador, Integer idServicoInterno, LocalDate dataServico) {
        String sql = "DELETE FROM servicosDoColaborador WHERE idColaborador = ? AND idServicoInterno = ? AND dataServico = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idColaborador);
            ps.setInt(2, idServicoInterno);
            ps.setDate(3, Date.valueOf(dataServico));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao remover serviço do colaborador.", e);
        }
    }
}
