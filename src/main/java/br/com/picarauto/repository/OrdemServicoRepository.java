package br.com.picarauto.repository;

import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.MecanicoModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.OrdemServicoPecaModel;
import br.com.picarauto.model.OrdemServicoServicoModel;
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.ServicoModel;
import br.com.picarauto.model.UsuarioModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.exception.BusinessException;
import br.com.picarauto.util.ConexaoBanco;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRepository implements IOrdemServicoRepository {

    private OrdemServicoModel mapRow(ResultSet rs) throws SQLException {
        OrdemServicoModel os = new OrdemServicoModel();
        os.setId(rs.getInt("id"));
        os.setAtivo(rs.getBoolean("ativo"));
        os.setNumero(rs.getLong("numero"));
        os.setDescricaoProblema(rs.getString("descricao_problema"));
        os.setStatusOrdemServico(OrdemServicoModel.StatusOrdemServico.valueOf(rs.getString("status")));
        os.setObservacoes(rs.getString("observacoes"));
        os.setValorMaoDeObra(rs.getBigDecimal("valor_mao_de_obra"));
        os.setValorPecas(rs.getBigDecimal("valor_pecas"));
        os.setValorDeslocamento(rs.getBigDecimal("valor_deslocamento"));
        os.setValorGincho(rs.getBigDecimal("valor_gincho"));
        os.setValorOutros(rs.getBigDecimal("valor_outros"));
        os.setDesconto(rs.getBigDecimal("desconto"));

        String dataAbertura = rs.getString("data_abertura");
        if (dataAbertura != null) os.setDataAbertura(LocalDate.parse(dataAbertura));

        String dataConclusao = rs.getString("data_conclusao");
        if (dataConclusao != null) os.setDataConclusao(LocalDate.parse(dataConclusao));

        String dataEntrada = rs.getString("data_entrada");
        if (dataEntrada != null) os.setDataEntrada(LocalDate.parse(dataEntrada));

        int clienteId = rs.getInt("cliente_id");
        if (!rs.wasNull()) { ClienteModel c = new ClienteModel(); c.setId(clienteId); os.setCliente(c); }

        int veiculoId = rs.getInt("veiculo_id");
        if (!rs.wasNull()) { VeiculoModel v = new VeiculoModel(); v.setId(veiculoId); os.setVeiculo(v); }

        int mecanicoId = rs.getInt("mecanico_responsavel_id");
        if (!rs.wasNull()) { MecanicoModel m = new MecanicoModel(); m.setId(mecanicoId); os.setMecanicoResponsavel(m); }

        int usuarioId = rs.getInt("usuario_responsavel_id");
        if (!rs.wasNull()) { UsuarioModel u = new UsuarioModel(); u.setId(usuarioId); os.setUsuarioResponsavel(u); }

        return os;
    }

    @Override
    public OrdemServicoModel findByIdAndAtivoTrue(Integer id) {
        String sql = "SELECT * FROM ordem_servico WHERE id = ? AND ativo = 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                OrdemServicoModel os = mapRow(rs);
                os.setServicosExecutados(findServicos(conn, id));
                os.setPecasAplicadas(findPecas(conn, id));
                return os;
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao buscar ordem de serviço.", e);
        }
    }

    @Override
    public List<OrdemServicoModel> findAllByAtivoTrue() {
        String sql = "SELECT * FROM ordem_servico WHERE ativo = 1 ORDER BY numero DESC";
        List<OrdemServicoModel> list = new ArrayList<>();
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrdemServicoModel os = mapRow(rs);
                os.setServicosExecutados(findServicos(conn, os.getId()));
                os.setPecasAplicadas(findPecas(conn, os.getId()));
                list.add(os);
            }
            return list;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao listar ordens de serviço.", e);
        }
    }

    @Override
    public OrdemServicoModel save(OrdemServicoModel os) {
        if (os.getId() == null) {
            String sql = "INSERT INTO ordem_servico (numero, descricao_problema, status, data_abertura, data_entrada, cliente_id, veiculo_id, mecanico_responsavel_id, usuario_responsavel_id, valor_mao_de_obra, valor_pecas, valor_deslocamento, valor_gincho, valor_outros, desconto, observacoes, ativo, data_hora_criacao) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, os.getNumero());
                ps.setString(2, os.getDescricaoProblema());
                ps.setString(3, os.getStatusOrdemServico().name());
                ps.setString(4, os.getDataAbertura() != null ? os.getDataAbertura().toString() : null);
                ps.setString(5, os.getDataEntrada() != null ? os.getDataEntrada().toString() : null);
                ps.setObject(6, os.getCliente() != null ? os.getCliente().getId() : null);
                ps.setObject(7, os.getVeiculo() != null ? os.getVeiculo().getId() : null);
                ps.setObject(8, os.getMecanicoResponsavel() != null ? os.getMecanicoResponsavel().getId() : null);
                ps.setObject(9, os.getUsuarioResponsavel() != null ? os.getUsuarioResponsavel().getId() : null);
                ps.setBigDecimal(10, os.getValorMaoDeObra());
                ps.setBigDecimal(11, os.getValorPecas());
                ps.setBigDecimal(12, os.getValorDeslocamento());
                ps.setBigDecimal(13, os.getValorGincho());
                ps.setBigDecimal(14, os.getValorOutros());
                ps.setBigDecimal(15, os.getDesconto());
                ps.setString(16, os.getObservacoes());
                ps.setBoolean(17, os.isAtivo());
                ps.setTimestamp(18, new Timestamp(os.getDataHoraCriacao().getTime()));
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) os.setId(keys.getInt(1));
                saveServicos(conn, os);
                savePecas(conn, os);
                return os;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "ordem de serviço");
                return null;
            }
        } else {
            String sql = "UPDATE ordem_servico SET descricao_problema=?, status=?, data_abertura=?, data_conclusao=?, data_entrada=?, cliente_id=?, veiculo_id=?, mecanico_responsavel_id=?, usuario_responsavel_id=?, valor_mao_de_obra=?, valor_pecas=?, valor_deslocamento=?, valor_gincho=?, valor_outros=?, desconto=?, observacoes=?, ativo=? WHERE id=?";
            try (Connection conn = ConexaoBanco.getConexao();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, os.getDescricaoProblema());
                ps.setString(2, os.getStatusOrdemServico().name());
                ps.setString(3, os.getDataAbertura() != null ? os.getDataAbertura().toString() : null);
                ps.setString(4, os.getDataConclusao() != null ? os.getDataConclusao().toString() : null);
                ps.setString(5, os.getDataEntrada() != null ? os.getDataEntrada().toString() : null);
                ps.setObject(6, os.getCliente() != null ? os.getCliente().getId() : null);
                ps.setObject(7, os.getVeiculo() != null ? os.getVeiculo().getId() : null);
                ps.setObject(8, os.getMecanicoResponsavel() != null ? os.getMecanicoResponsavel().getId() : null);
                ps.setObject(9, os.getUsuarioResponsavel() != null ? os.getUsuarioResponsavel().getId() : null);
                ps.setBigDecimal(10, os.getValorMaoDeObra());
                ps.setBigDecimal(11, os.getValorPecas());
                ps.setBigDecimal(12, os.getValorDeslocamento());
                ps.setBigDecimal(13, os.getValorGincho());
                ps.setBigDecimal(14, os.getValorOutros());
                ps.setBigDecimal(15, os.getDesconto());
                ps.setString(16, os.getObservacoes());
                ps.setBoolean(17, os.isAtivo());
                ps.setInt(18, os.getId());
                ps.executeUpdate();
                saveServicos(conn, os);
                savePecas(conn, os);
                return os;
            } catch (SQLException e) {
                BusinessException.handleSQLException(e, "ordem de serviço");
                return null;
            }
        }
    }

    private void saveServicos(Connection conn, OrdemServicoModel os) throws SQLException {
        String del = "DELETE FROM ordem_servico_servico WHERE ordem_servico_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(del)) {
            ps.setInt(1, os.getId());
            ps.executeUpdate();
        }
        if (os.getServicosExecutados() == null) return;
        String ins = "INSERT INTO ordem_servico_servico (ordem_servico_id, servico_id, mecanico_executor_id, horas_executadas, valor_cobrado, horario_inicio, horario_fim) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            for (OrdemServicoServicoModel oss : os.getServicosExecutados()) {
                ps.setInt(1, os.getId());
                ps.setObject(2, oss.getServico() != null ? oss.getServico().getId() : null);
                ps.setObject(3, oss.getMecanicoExecutor() != null ? oss.getMecanicoExecutor().getId() : null);
                ps.setBigDecimal(4, oss.getHorasExecutadas());
                ps.setBigDecimal(5, oss.getValorCobrado());
                ps.setTimestamp(6, oss.getHorarioInicio() != null ? new Timestamp(oss.getHorarioInicio().getTime()) : null);
                ps.setTimestamp(7, oss.getHorarioFim() != null ? new Timestamp(oss.getHorarioFim().getTime()) : null);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void savePecas(Connection conn, OrdemServicoModel os) throws SQLException {
        String del = "DELETE FROM ordem_servico_peca WHERE ordem_servico_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(del)) {
            ps.setInt(1, os.getId());
            ps.executeUpdate();
        }
        if (os.getPecasAplicadas() == null) return;
        String ins = "INSERT INTO ordem_servico_peca (ordem_servico_id, peca_id, quantidade, valor_unitario) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(ins)) {
            for (OrdemServicoPecaModel osp : os.getPecasAplicadas()) {
                ps.setInt(1, os.getId());
                ps.setObject(2, osp.getPeca() != null ? osp.getPeca().getId() : null);
                ps.setInt(3, osp.getQuantidade());
                ps.setBigDecimal(4, osp.getValorUnitario());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<OrdemServicoServicoModel> findServicos(Connection conn, Integer osId) throws SQLException {
        String sql = "SELECT * FROM ordem_servico_servico WHERE ordem_servico_id = ?";
        List<OrdemServicoServicoModel> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, osId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdemServicoServicoModel oss = new OrdemServicoServicoModel();
                oss.setId(rs.getInt("id"));
                ServicoModel s = new ServicoModel(); s.setId(rs.getInt("servico_id")); oss.setServico(s);
                int mecId = rs.getInt("mecanico_executor_id");
                if (!rs.wasNull()) { MecanicoModel m = new MecanicoModel(); m.setId(mecId); oss.setMecanicoExecutor(m); }
                oss.setHorasExecutadas(rs.getBigDecimal("horas_executadas"));
                oss.setValorCobrado(rs.getBigDecimal("valor_cobrado"));
                Timestamp ini = rs.getTimestamp("horario_inicio");
                if (ini != null) oss.setHorarioInicio(new java.util.Date(ini.getTime()));
                Timestamp fim = rs.getTimestamp("horario_fim");
                if (fim != null) oss.setHorarioFim(new java.util.Date(fim.getTime()));
                list.add(oss);
            }
        }
        return list;
    }

    private List<OrdemServicoPecaModel> findPecas(Connection conn, Integer osId) throws SQLException {
        String sql = "SELECT * FROM ordem_servico_peca WHERE ordem_servico_id = ?";
        List<OrdemServicoPecaModel> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, osId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdemServicoPecaModel osp = new OrdemServicoPecaModel();
                osp.setId(rs.getInt("id"));
                PecaModel p = new PecaModel(); p.setId(rs.getInt("peca_id")); osp.setPeca(p);
                osp.setQuantidade(rs.getInt("quantidade"));
                osp.setValorUnitario(rs.getBigDecimal("valor_unitario"));
                list.add(osp);
            }
        }
        return list;
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM ordem_servico WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new BusinessException("Erro ao verificar ordem de serviço.", e);
        }
    }

    @Override
    public Long gerarProximoNumero() {
        String sql = "SELECT COALESCE(MAX(numero), 0) + 1 FROM ordem_servico";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
            return 1L;
        } catch (SQLException e) {
            throw new BusinessException("Erro ao gerar número da OS.", e);
        }
    }
}