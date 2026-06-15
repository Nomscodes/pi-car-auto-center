// ╔═══════════════════════════════════════════════════════╗
// PanelComposicaoOS.java
// ╚═══════════════════════════════════════════════════════╝
package br.com.picarauto.view;

/**
 * Composição de Ordem de Serviço — formulário interativo completo.
 * Cliente, colaborador, veículo, serviços, peças, observações e total dinâmico.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.OrdemServicoController;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.controller.ColaboradorController;
import br.com.picarauto.controller.ItemPedidoServicoExternoController;
import br.com.picarauto.controller.ItemServicoInternoController;
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.MarcaController;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.controller.PecaController;
import br.com.picarauto.controller.ServicoInternoController;
import br.com.picarauto.controller.ServicoExternoController;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.ItemPedidoServicoExternoModel;
import br.com.picarauto.model.ItemServicoInternoModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import java.util.ArrayList;
import java.util.List;

public class PanelComposicaoOS extends JPanel {

    private final MainFrame frame;

    private List<ClienteModel>        clientesDisponiveis      = new ArrayList<>();
    private List<ColaboradorModel>    colaboradoresDisponiveis = new ArrayList<>();
    private List<ItemServicoInternoModel> itensServicoInterno = new ArrayList<>();
    private List<ItemPedidoServicoExternoModel> itensServicoExterno = new ArrayList<>();
    private List<VeiculoModel>        veiculosDisponiveis      = new ArrayList<>();
    private List<PecaModel>           pecasDisponiveis         = new ArrayList<>();
    private List<ServicoInternoModel> servicosInternos         = new ArrayList<>();
    private List<ServicoExternoModel> servicosExternos         = new ArrayList<>();
    private List<MarcaModel>          marcasDisponiveis        = new ArrayList<>();
    private List<ModeloModel>         modelosDisponiveis       = new ArrayList<>();

    private static final NumberFormat FMT_MOEDA =
        NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private JComboBox<String> cmbCliente, cmbColaborador, cmbMarca, cmbModelo, cmbStatus, cmbPlaca;
    private JTextField        txtData;
    private DefaultTableModel modeloServicos, modeloPecas;
    private JLabel            lblTotal;
    private JTextArea         txtObs;

    public PanelComposicaoOS(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_COMPOSICAO), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  \u2014  Nova Ordem de Servi\u00e7o");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        // BOTÃO TELA COMPOSIÇÃO OS DE VOLTAR PARA LISTA DE OS
        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        bar.add(lbl,       BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JScrollPane criarScrollConteudo() {
        JPanel p = new JPanel();
        p.setBackground(MainFrame.COR_CREAM);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        p.add(criarLabelSecao("Dados da OS"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardDados());
        p.add(Box.createVerticalStrut(20));

        p.add(criarLabelSecao("Servi\u00e7os"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardServicos());
        p.add(Box.createVerticalStrut(20));

        p.add(criarLabelSecao("Pe\u00e7as Utilizadas"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardPecas());
        p.add(Box.createVerticalStrut(20));

        p.add(criarLabelSecao("Observa\u00e7\u00f5es"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardObservacoes());
        p.add(Box.createVerticalStrut(24));

        p.add(criarRodapeAcoes());

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    public void carregarDados() {
        carregarClientes();
        carregarColaboradores();
        carregarServicos();
        carregarPecas();
        carregarMarcas();
        limparFormulario();
    }

    private void carregarClientes() {
        try {
            ClienteController cc = ContextoAplicacao.getBean(ClienteController.class);
            clientesDisponiveis = cc.findAll();
            cmbCliente.removeAllItems();
            cmbCliente.addItem("Selecione...");
            for (ClienteModel c : clientesDisponiveis)
                cmbCliente.addItem(capitalizarNome(c.getNomeCompleto()));
        } catch (Exception ex) {
            cmbCliente.removeAllItems();
            cmbCliente.addItem("Erro ao carregar");
        }
    }

    private void carregarColaboradores() {
        try {
            ColaboradorController cc = ContextoAplicacao.getBean(ColaboradorController.class);
            colaboradoresDisponiveis = cc.findAll();
            cmbColaborador.removeAllItems();
            cmbColaborador.addItem("Selecione...");
            for (ColaboradorModel c : colaboradoresDisponiveis)
                cmbColaborador.addItem(capitalizarNome(c.getNomeCompleto()));
        } catch (Exception ex) {
            cmbColaborador.removeAllItems();
            cmbColaborador.addItem("Erro ao carregar");
        }
    }

    private void carregarServicos() {
        try {
            ServicoInternoController sic = ContextoAplicacao.getBean(ServicoInternoController.class);
            servicosInternos = sic.findAll();
        } catch (Exception ex) {
            servicosInternos = new ArrayList<>();
        }
        try {
            ServicoExternoController sec = ContextoAplicacao.getBean(ServicoExternoController.class);
            servicosExternos = sec.findAll();
        } catch (Exception ex) {
            servicosExternos = new ArrayList<>();
        }
    }

    private void carregarPecas() {
        try {
            PecaController pc = ContextoAplicacao.getBean(PecaController.class);
            pecasDisponiveis = pc.findAll();
        } catch (Exception ex) {
            pecasDisponiveis = new ArrayList<>();
        }
    }

    private void carregarMarcas() {
        try {
            MarcaController mc = ContextoAplicacao.getBean(MarcaController.class);
            marcasDisponiveis = mc.findAll();
            cmbMarca.removeAllItems();
            cmbMarca.addItem("Selecione...");
            for (MarcaModel m : marcasDisponiveis)
                cmbMarca.addItem(m.getNome());
        } catch (Exception ex) {
            cmbMarca.removeAllItems();
            cmbMarca.addItem("Erro ao carregar");
        }
    }

    private void carregarModelosDaMarca(Long idMarca) {
        try {
            ModeloController mc = ContextoAplicacao.getBean(ModeloController.class);
            modelosDisponiveis = mc.findAllByIdMarca(idMarca);
            cmbModelo.removeAllItems();
            cmbModelo.setEnabled(true);
            cmbModelo.addItem("Selecione o modelo...");
            for (ModeloModel m : modelosDisponiveis)
                cmbModelo.addItem(m.getNomeModelo() + " (" + m.getAnoModelo() + ")");
        } catch (Exception ex) {
            cmbModelo.removeAllItems();
            cmbModelo.addItem("Erro ao carregar");
        }
    }

    private void limparFormulario() {
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cmbPlaca.removeAllItems();
        cmbPlaca.addItem("Selecione...");
        if (txtObs != null) txtObs.setText("");
        modeloServicos.setRowCount(0);
        modeloPecas.setRowCount(0);
        lblTotal.setText("R$ 0,00");
        cmbMarca.setSelectedIndex(0);
        cmbModelo.removeAllItems();
        cmbModelo.addItem("Selecione primeiro a marca...");
        cmbModelo.setEnabled(false);
        cmbStatus.setSelectedIndex(0);
    }

    private JPanel criarCardDados() {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        cmbCliente     = criarCombo(new String[]{"Selecione..."});
        cmbColaborador = criarCombo(new String[]{"Selecione..."});
        cmbMarca       = criarCombo(new String[]{"Carregando..."});
        cmbModelo      = criarCombo(new String[]{"Selecione primeiro a marca..."});
        cmbModelo.setEnabled(false);

        cmbStatus = criarCombo(new String[]{
            "ORCAMENTO", "EXECUCAO", "PAGAMENTO", "FINALIZADO"
        });

        cmbPlaca = criarCombo(new String[]{"Selecione..."});
        txtData  = criarCampo();
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        cmbMarca.addActionListener(e -> {
            int idx = cmbMarca.getSelectedIndex();
            cmbModelo.removeAllItems();
            if (idx > 0 && idx <= marcasDisponiveis.size()) {
                MarcaModel marcaSel = marcasDisponiveis.get(idx - 1);
                carregarModelosDaMarca(marcaSel.getId());
            } else {
                cmbModelo.setEnabled(false);
                cmbModelo.addItem("Selecione primeiro a marca...");
            }
        });

        cmbPlaca.addActionListener(e -> {
            int idxPlaca = cmbPlaca.getSelectedIndex();
            if (idxPlaca <= 0 || veiculosDisponiveis.isEmpty() || idxPlaca > veiculosDisponiveis.size()) return;
            VeiculoModel veiculo = veiculosDisponiveis.get(idxPlaca - 1);
            atualizarMarcaModeloPorVeiculo(veiculo);
        });

        cmbCliente.addActionListener(e -> {
            int idx = cmbCliente.getSelectedIndex();
            if (idx <= 0 || clientesDisponiveis.isEmpty() || idx > clientesDisponiveis.size()) return;
            ClienteModel clienteSel = clientesDisponiveis.get(idx - 1);
            carregarVeiculosDoCliente(clienteSel.getId());
        });

        JPanel row1 = criarGridRow(2);
        row1.add(criarGrupoCombo("Cliente",      cmbCliente));
        row1.add(criarGrupoCombo("Colaborador",  cmbColaborador));

        JPanel row2 = criarGridRow(2);
        row2.add(criarGrupoCombo("Marca",  cmbMarca));
        row2.add(criarGrupoCombo("Modelo", cmbModelo));

        JPanel row3 = criarGridRow(3);
        row3.add(criarGrupoCombo("Placa do ve\u00edculo", cmbPlaca));
        row3.add(criarGrupoCampo("Data Abertura",    txtData));
        row3.add(criarGrupoCombo("Status",           cmbStatus));

        card.add(row1);
        card.add(Box.createVerticalStrut(12));
        card.add(row2);
        card.add(Box.createVerticalStrut(12));
        card.add(row3);
        return card;
    }

    private void atualizarMarcaModeloPorVeiculo(VeiculoModel veiculo) {
        try {
            ModeloController mc = ContextoAplicacao.getBean(ModeloController.class);
            ModeloModel modelo = mc.findAll().stream()
                .filter(m -> m.getId().equals(veiculo.getIdModelo()))
                .findFirst().orElse(null);
            if (modelo == null) return;

            for (int i = 0; i < marcasDisponiveis.size(); i++) {
                if (marcasDisponiveis.get(i).getId().equals(modelo.getIdMarca())) {
                    cmbMarca.setSelectedIndex(i + 1);
                    break;
                }
            }

            carregarModelosDaMarca(modelo.getIdMarca());
            for (int i = 0; i < modelosDisponiveis.size(); i++) {
                if (modelosDisponiveis.get(i).getId().equals(modelo.getId())) {
                    cmbModelo.setSelectedIndex(i + 1);
                    break;
                }
            }
        } catch (Exception ex) {
            // silencia
        }
    }

    private void carregarVeiculosDoCliente(Long idCliente) {
        try {
            VeiculoController vc = ContextoAplicacao.getBean(VeiculoController.class);
            veiculosDisponiveis = vc.findAll().stream()
                .filter(v -> idCliente != null && idCliente.equals(v.getIdCliente()))
                .toList();
            cmbPlaca.removeAllItems();
            cmbPlaca.addItem("Selecione...");
            for (VeiculoModel v : veiculosDisponiveis)
                cmbPlaca.addItem(formatarPlaca(v.getPlaca()));
            if (veiculosDisponiveis.size() == 1)
                cmbPlaca.setSelectedIndex(1);
        } catch (Exception ex) {
            veiculosDisponiveis = new ArrayList<>();
        }
    }

    private JPanel criarCardServicos() {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        modeloServicos = new DefaultTableModel(new String[]{"Servi\u00e7o", "Tipo", "Valor"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JScrollPane scrollServ = new JScrollPane(criarTabela(modeloServicos));
        scrollServ.setBorder(null);
        scrollServ.getViewport().setBackground(Color.WHITE);
        scrollServ.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollServ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JTable tabelaServicos = criarTabela(modeloServicos);
        scrollServ = new JScrollPane(tabelaServicos);
        scrollServ.setBorder(null);
        scrollServ.getViewport().setBackground(Color.WHITE);

        // BOTÃO TELA COMPOSIÇÃO OS DE EXCLUIR SERVIÇO DA TABELA
        JButton btnExcluirServ = criarBotaoOutline("Excluir", 90, 32);
        btnExcluirServ.addActionListener(e -> {
            int row = tabelaServicos.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um servi\u00e7o para excluir.", "Aten\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modeloServicos.removeRow(row);
            recalcularTotal();
        });

        // BOTÃO TELA COMPOSIÇÃO OS DE ADICIONAR SERVIÇO
        JButton btnAdd = criarBotaoNavy("+ Adicionar servi\u00e7o", 160, 32);
        btnAdd.addActionListener(e -> adicionarServico());
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(btnExcluirServ);
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(btnAdd);

        card.add(scrollServ);
        card.add(toolbar);
        return card;
    }

    private JPanel criarCardPecas() {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        modeloPecas = new DefaultTableModel(new String[]{"Pe\u00e7a", "Qtd", "Valor Unit.", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabelaPecas = criarTabela(modeloPecas);
        JScrollPane scrollPec = new JScrollPane(tabelaPecas);
        scrollPec.setBorder(null);
        scrollPec.getViewport().setBackground(Color.WHITE);
        scrollPec.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPec.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        toolbar.setBorder(new EmptyBorder(8, 8, 8, 8));

        // BOTÃO TELA COMPOSIÇÃO OS DE EXCLUIR PEÇA DA TABELA
        JButton btnExcluirPec = criarBotaoOutline("Excluir", 90, 32);
        btnExcluirPec.addActionListener(e -> {
            int row = tabelaPecas.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma pe\u00e7a para excluir.", "Aten\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modeloPecas.removeRow(row);
            recalcularTotal();
        });

        // BOTÃO TELA COMPOSIÇÃO OS DE ADICIONAR PEÇA
        JButton btnAdd = criarBotaoNavy("+ Adicionar pe\u00e7a", 150, 32);
        btnAdd.addActionListener(e -> adicionarPeca());
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(btnExcluirPec);
        toolbar.add(Box.createHorizontalStrut(8));
        toolbar.add(btnAdd);

        card.add(scrollPec);
        card.add(toolbar);
        return card;
    }

    private JPanel criarCardObservacoes() {
        JPanel card = criarCardBase();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtObs = new JTextArea(3, 0);
        txtObs.setFont(MainFrame.FONT_NORMAL);
        txtObs.setBackground(Color.WHITE);
        txtObs.setBorder(null);
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        txtObs.setForeground(new Color(0x444444));
        card.add(txtObs, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarRodapeAcoes() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel totalPanel = new JPanel();
        totalPanel.setOpaque(false);
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));

        JLabel lblTotalLabel = new JLabel("Total estimado");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTotalLabel.setForeground(new Color(0x666666));

        lblTotal = new JLabel("R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(MainFrame.COR_NAVY);

        totalPanel.add(lblTotalLabel);
        totalPanel.add(lblTotal);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        // BOTÃO TELA COMPOSIÇÃO OS DE CANCELAR E VOLTAR PARA LISTA DE OS
        JButton btnCancelar = criarBotaoOutline("Cancelar", 110, 38);
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        // BOTÃO TELA COMPOSIÇÃO OS DE VER RESUMO DA OS (Decorator)
        // Padrão Decorator — gera resumo da OS em camadas
        JButton btnResumo = criarBotaoNavy("Ver Resumo", 130, 38);
        btnResumo.addActionListener(e -> {
            OrdemServicoModel osResumo = new OrdemServicoModel();
            osResumo.setStatus(OrdemServicoModel.StatusOrdemServico.valueOf(
                (String) cmbStatus.getSelectedItem()));
            osResumo.setObservacoes(txtObs != null ? txtObs.getText().trim() : "");
            osResumo.setDataAbertura(java.time.LocalDate.now());
            String totalStr = lblTotal.getText().replaceAll("[^\\d,]", "").replace(",", ".");
            try { osResumo.setValorTotal(Double.parseDouble(totalStr)); }
            catch (NumberFormatException ignored) { osResumo.setValorTotal(0.0); }
            if (cmbPlaca.getSelectedIndex() > 0)
                osResumo.setPlacaVeiculo(cmbPlaca.getSelectedItem().toString());

            br.com.picarauto.decorator.IResumoOS resumo =
                new br.com.picarauto.decorator.ResumoOSBase(osResumo);
            String texto = resumo.gerar();

            JTextArea area = new JTextArea(texto, 12, 40);
            area.setEditable(false);
            area.setFont(new Font("Courier New", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this,
                new JScrollPane(area), "Resumo da OS", JOptionPane.INFORMATION_MESSAGE);
        });

        // BOTÃO TELA COMPOSIÇÃO OS DE SALVAR NOVA OS NO BANCO
        JButton btnSalvar = criarBotaoGold("Salvar OS", 140, 38);
        btnSalvar.addActionListener(e -> salvarOS());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnResumo);
        btnPanel.add(btnSalvar);

        p.add(totalPanel, BorderLayout.WEST);
        p.add(btnPanel,   BorderLayout.EAST);
        return p;
    }

    private void salvarOS() {
        try {
            int idxCliente = cmbCliente.getSelectedIndex();
            if (idxCliente <= 0 || clientesDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Aten\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ClienteModel cliente = clientesDisponiveis.get(idxCliente - 1);

            if (cmbPlaca.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione a placa do ve\u00edculo.", "Aten\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String placaDigitada = cmbPlaca.getSelectedItem().toString().replace("-", "").toUpperCase().trim();

            VeiculoController vc = ContextoAplicacao.getBean(VeiculoController.class);
            List<VeiculoModel> todosVeiculos = vc.findAll();
            VeiculoModel veiculoEncontrado = null;
            for (VeiculoModel v : todosVeiculos) {
                if (v.getPlaca().equalsIgnoreCase(placaDigitada)) {
                    veiculoEncontrado = v;
                    break;
                }
            }

            if (veiculoEncontrado == null) {
                JOptionPane.showMessageDialog(this,
                    "Ve\u00edculo com a placa informada n\u00e3o encontrado no cadastro.",
                    "Aten\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
                return;
            }

            OrdemServicoModel os = new OrdemServicoModel();
            os.setIdVeiculo(veiculoEncontrado.getId());
            os.setDataAbertura(LocalDate.now());
            os.setObservacoes(txtObs != null ? txtObs.getText().trim() : "");

            String statusSel = (String) cmbStatus.getSelectedItem();
            os.setStatus(OrdemServicoModel.StatusOrdemServico.valueOf(statusSel));

            double total = 0;
            for (int i = 0; i < modeloServicos.getRowCount(); i++) {
                try {
                    total += Double.parseDouble(modeloServicos.getValueAt(i, 2).toString()
                        .replaceAll("[^\\d,]", "").replace(",", "."));
                } catch (NumberFormatException ignored) {}
            }
            for (int i = 0; i < modeloPecas.getRowCount(); i++) {
                try {
                    total += Double.parseDouble(modeloPecas.getValueAt(i, 3).toString()
                        .replaceAll("[^\\d,]", "").replace(",", "."));
                } catch (NumberFormatException ignored) {}
            }
            os.setValorTotal(total > 0 ? total : null);

            os.setPlacaVeiculo(veiculoEncontrado.getPlaca());
            os.setNomeCliente(cliente.getNomeCompleto());

            OrdemServicoController osc = ContextoAplicacao.getBean(OrdemServicoController.class);
            OrdemServicoModel osSalva = osc.insert(os);

            ItemServicoInternoController itemIntCtrl =
                ContextoAplicacao.getBean(ItemServicoInternoController.class);
            for (ItemServicoInternoModel item : itensServicoInterno) {
                item.setIdOS(osSalva.getId());
                itemIntCtrl.insert(item);
            }

            ItemPedidoServicoExternoController itemExtCtrl =
                ContextoAplicacao.getBean(ItemPedidoServicoExternoController.class);
            for (ItemPedidoServicoExternoModel item : itensServicoExterno) {
                item.setIdOS(osSalva.getId());
                itemExtCtrl.insert(item);
            }

            JOptionPane.showMessageDialog(this, "OS salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            frame.mostrarTela(MainFrame.TELA_LISTA_OS);

        } catch (FieldValidationException | RuleValidationException valEx) {
            JOptionPane.showMessageDialog(this, valEx.getMessage(), "Erro de valida\u00e7\u00e3o", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar OS: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    private void adicionarServico() {
        List<String> nomes = new ArrayList<>();
        if (servicosInternos.isEmpty() && servicosExternos.isEmpty()) {
            nomes.add("Nenhum servi\u00e7o cadastrado");
        }
        for (ServicoInternoModel s : servicosInternos) nomes.add("[INT] " + s.getDescricao());
        for (ServicoExternoModel s : servicosExternos) nomes.add("[EXT] " + s.getDescricao());

        JComboBox<String> cmbServico = new JComboBox<>(nomes.toArray(new String[0]));
        cmbServico.setFont(MainFrame.FONT_NORMAL);

        JTextField txtValor = new JTextField("0,00", 10);
        aplicarMascaraMonetaria(txtValor);

        cmbServico.addActionListener(e -> {
            int idx = cmbServico.getSelectedIndex();
            if (servicosInternos.isEmpty() && servicosExternos.isEmpty()) return;
            double valor = 0;
            if (idx < servicosInternos.size()) {
                valor = servicosInternos.get(idx).getValorCobrado();
            } else {
                int idxExt = idx - servicosInternos.size();
                if (idxExt < servicosExternos.size())
                    valor = servicosExternos.get(idxExt).getValorCobrado();
            }
            txtValor.setText(String.format("%.2f", valor).replace(".", ","));
        });

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Selecione o servi\u00e7o:")); form.add(cmbServico);
        form.add(new JLabel("Valor (R$):"));               form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Servi\u00e7o",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

            if (servicosInternos.isEmpty() && servicosExternos.isEmpty()) return;

            int idx = cmbServico.getSelectedIndex();
            String nome;
            String tipo;
            double valorDouble = parseMoeda(txtValor.getText());

            if (idx < servicosInternos.size()) {
                ServicoInternoModel catalogo = servicosInternos.get(idx);

                ItemServicoInternoController ctrl =
                    ContextoAplicacao.getBean(ItemServicoInternoController.class);

                ItemServicoInternoModel item = ctrl.novoItem(); // Factory Method
                item.setValorItem(valorDouble);
                item.setGarantia(0);
                item.setObservacoes(catalogo.getDescricao());

                itensServicoInterno.add(item);
                nome = "[INT] " + catalogo.getDescricao();
                tipo = "Interno";

            } else {
                int idxExt = idx - servicosInternos.size();
                ServicoExternoModel catalogo = servicosExternos.get(idxExt);

                ItemPedidoServicoExternoController ctrl =
                    ContextoAplicacao.getBean(ItemPedidoServicoExternoController.class);

                ItemPedidoServicoExternoModel item = ctrl.novoItem(); // Factory Method
                item.setValorItem(valorDouble);
                item.setGarantia(0);
                item.setObservacoes(catalogo.getDescricao());

                itensServicoExterno.add(item);
                nome = "[EXT] " + catalogo.getDescricao();
                tipo = "Externo";
            }

            String valorStr = FMT_MOEDA.format(valorDouble);
            modeloServicos.addRow(new Object[]{ nome, tipo, valorStr });
            recalcularTotal();
        }
    }

    private void adicionarPeca() {
        List<String> nomesPecas = new ArrayList<>();
        if (pecasDisponiveis.isEmpty()) {
            nomesPecas.add("Nenhuma pe\u00e7a cadastrada");
        }
        for (PecaModel p : pecasDisponiveis)
            nomesPecas.add(p.getMarca() + " \u2014 " + p.getModelo() + " (" + p.getAnoVeiculo() + ")");

        JComboBox<String> cmbPeca = new JComboBox<>(nomesPecas.toArray(new String[0]));
        cmbPeca.setFont(MainFrame.FONT_NORMAL);

        JTextField txtQtd = new JTextField("1", 5);
        ((AbstractDocument) txtQtd.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet a)
                    throws BadLocationException {
                if (str.matches("\\d+")) super.insertString(fb, off, str, a);
            }
            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet a)
                    throws BadLocationException {
                if (str.matches("\\d*")) super.replace(fb, off, len, str, a);
            }
        });

        JTextField txtValor = new JTextField("0,00", 10);
        aplicarMascaraMonetaria(txtValor);

        cmbPeca.addActionListener(e -> {
            int idx = cmbPeca.getSelectedIndex();
            if (!pecasDisponiveis.isEmpty() && idx < pecasDisponiveis.size()) {
                double preco = pecasDisponiveis.get(idx).getPrecoUnitario();
                txtValor.setText(String.format("%.2f", preco).replace(".", ","));
            }
        });

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Selecione a pe\u00e7a:")); form.add(cmbPeca);
        form.add(new JLabel("Quantidade:"));            form.add(txtQtd);
        form.add(new JLabel("Valor Unit. (R$):"));      form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Pe\u00e7a",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

            if (pecasDisponiveis.isEmpty()) return;

            String nomePeca = (String) cmbPeca.getSelectedItem();
            int qtd = 1;
            try { qtd = Math.max(1, Integer.parseInt(txtQtd.getText().trim())); }
            catch (NumberFormatException ignored) {}

            double valor = parseMoeda(txtValor.getText());
            String valorUnitStr = FMT_MOEDA.format(valor);
            String totalStr     = FMT_MOEDA.format(valor * qtd);
            modeloPecas.addRow(new Object[]{ nomePeca, qtd, valorUnitStr, totalStr });
            recalcularTotal();
        }
    }

    // ── Helpers de valor ──────────────────────────────────────────────────────

    private void aplicarMascaraMonetaria(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet a)
                    throws BadLocationException {
                if (str.matches("[\\d,\\.]*")) super.insertString(fb, off, str, a);
            }
            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet a)
                    throws BadLocationException {
                if (str.matches("[\\d,\\.]*")) super.replace(fb, off, len, str, a);
            }
        });
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                double v = parseMoeda(campo.getText());
                campo.setText(String.format("%,.2f", v).replace(".", "#").replace(",", ".").replace("#", ","));
            }
        });
    }

    private double parseMoeda(String texto) {
        if (texto == null || texto.isBlank()) return 0;
        String limpo = texto.replaceAll("[^\\d,]", "").replace(",", ".");
        try { return Double.parseDouble(limpo); }
        catch (NumberFormatException ignored) { return 0; }
    }

    private void recalcularTotal() {
        double total = 0;
        for (int i = 0; i < modeloServicos.getRowCount(); i++) {
            total += parseMoeda(modeloServicos.getValueAt(i, 2).toString());
        }
        for (int i = 0; i < modeloPecas.getRowCount(); i++) {
            total += parseMoeda(modeloPecas.getValueAt(i, 3).toString());
        }
        lblTotal.setText(FMT_MOEDA.format(total));
    }

    // ── Helpers de máscara ────────────────────────────────────────────────────

    private static String formatarPlaca(String placa) {
        if (placa == null || placa.length() < 7) return placa != null ? placa : "\u2014";
        String p = placa.toUpperCase().replace("-", "").trim();
        if (p.length() == 7) return p.substring(0, 3) + "-" + p.substring(3);
        return placa;
    }

    private static String capitalizarNome(String nome) {
        if (nome == null || nome.isBlank()) return nome;
        String[] partes = nome.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : partes) {
            if (!p.isEmpty()) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
            }
        }
        return sb.toString();
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────

    private JLabel criarLabelSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainFrame.COR_NAVY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarGridRow(int cols) {
        JPanel row = new JPanel(new GridLayout(1, cols, 14, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        return row;
    }

    private JPanel criarCardBase() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return card;
    }

    private JTable criarTabela(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(MainFrame.FONT_NORMAL);
        t.setRowHeight(36);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(Color.WHITE);
        t.setSelectionBackground(new Color(0xe8e3d8));
        t.setSelectionForeground(MainFrame.COR_NAVY);
        t.setFillsViewportHeight(true);
        t.setDefaultEditor(Object.class, null);

        JTableHeader header = t.getTableHeader();
        header.setBackground(MainFrame.COR_CREAM_ALT);
        header.setForeground(new Color(0x444444));
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainFrame.COR_BORDER));
        header.setReorderingAllowed(false);
        return t;
    }

    private JPanel criarGrupoCombo(String label, JComboBox<String> combo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        combo.setFont(MainFrame.FONT_NORMAL);
        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(combo);
        return g;
    }

    private JPanel criarGrupoCampo(String label, JTextField campo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(campo);
        return g;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField();
        f.setFont(MainFrame.FONT_NORMAL);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34));
        return f;
    }

    private JComboBox<String> criarCombo(String[] itens) {
        JComboBox<String> c = new JComboBox<>(itens);
        c.setFont(MainFrame.FONT_NORMAL);
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(0, 34));
        return c;
    }

    private JButton criarBotaoNavy(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoGold(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(MainFrame.COR_NAVY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("\u2190 Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
