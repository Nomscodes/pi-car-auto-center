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
public class HistoricoVeiculoRepository implements IHistoricoVeiculoRepository {

    //Persistência (INSERT) 
    @Override
    public void save(Integer idPessoa, Integer idVeiculo, LocalDate dataInicio, LocalDate dataFim) {
        String sql = "INSERT INTO historicoVeiculo (idPessoa, idVeiculo, dataInicio, dataFim) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPessoa);
            ps.setInt(2, idVeiculo);
            ps.setDate(3, Date.valueOf(dataInicio));
            if (dataFim != null) {
                ps.setDate(4, Date.valueOf(dataFim));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            BusinessException.handleSQLException(e, "histórico do veículo");
        }
    }

    //Consultas específicas 
    @Override
    public List<Integer> findIdVeiculoByIdPessoa(Integer idPessoa) {
        String sql = "SELECT idVeiculo FROM historicoVeiculo WHERE idPessoa = ?";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPessoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("idVeiculo"));
            }
            return ids;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar veículos por pessoa.", e);
        }
    }

    @Override
    public List<Integer> findIdPessoaByIdVeiculo(Integer idVeiculo) {
        String sql = "SELECT idPessoa FROM historicoVeiculo WHERE idVeiculo = ?";
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVeiculo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("idPessoa"));
            }
            return ids;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar pessoas por veículo.", e);
        }
    }

    //Verificações de existência
    @Override
    public boolean existsByIdPessoaAndIdVeiculo(Integer idPessoa, Integer idVeiculo) {
        String sql = "SELECT 1 FROM historicoVeiculo WHERE idPessoa = ? AND idVeiculo = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPessoa);
            ps.setInt(2, idVeiculo);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar histórico.", e);
        }
    }

    //Remoção (DELETE) 
    @Override
    public void delete(Integer idPessoa, Integer idVeiculo, LocalDate dataInicio) {
        String sql = "DELETE FROM historicoVeiculo WHERE idPessoa = ? AND idVeiculo = ? AND dataInicio = ?";
        try (Connection conn = ConexaoBanco.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPessoa);
            ps.setInt(2, idVeiculo);
            ps.setDate(3, Date.valueOf(dataInicio));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao remover histórico do veículo.", e);
        }
    }
}
