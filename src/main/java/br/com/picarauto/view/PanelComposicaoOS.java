// ═══════════════════════════════════════════════
// PanelComposicaoOS.java
// ═══════════════════════════════════════════════
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
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.MarcaController;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.controller.PecaController;
import br.com.picarauto.controller.ServicoInternoController;
import br.com.picarauto.controller.ServicoExternoController;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.ColaboradorModel;
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

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Nova Ordem de Serviço");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

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

        p.add(criarLabelSecao("Serviços"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardServicos());
        p.add(Box.createVerticalStrut(20));

        p.add(criarLabelSecao("Peças Utilizadas"));
        p.add(Box.createVerticalStrut(10));
        p.add(criarCardPecas());
        p.add(Box.createVerticalStrut(20));

        p.add(criarLabelSecao("Observações"));
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

        // Ao trocar marca → carrega modelos do banco e reseta placa/modelo
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

        // Ao trocar placa → resolve marca e modelo do veículo selecionado
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
        row3.add(criarGrupoCombo("Placa do veículo", cmbPlaca));
        row3.add(criarGrupoCampo("Data Abertura",    txtData));
        row3.add(criarGrupoCombo("Status",           cmbStatus));

        card.add(row1);
        card.add(Box.createVerticalStrut(12));
        card.add(row2);
        card.add(Box.createVerticalStrut(12));
        card.add(row3);
        return card;
    }

    /**
     * Dado um veículo, busca sua marca e modelo no banco e seleciona nos combos.
     */
    private void atualizarMarcaModeloPorVeiculo(VeiculoModel veiculo) {
        try {
            ModeloController mc = ContextoAplicacao.getBean(ModeloController.class);
            ModeloModel modelo = mc.findAll().stream()
                .filter(m -> m.getId().equals(veiculo.getIdModelo()))
                .findFirst().orElse(null);
            if (modelo == null) return;

            // Seleciona a marca no combo sem disparar carregarModelosDaMarca novamente
            for (int i = 0; i < marcasDisponiveis.size(); i++) {
                if (marcasDisponiveis.get(i).getId().equals(modelo.getIdMarca())) {
                    // Temporariamente remove listener, define índice e re-adiciona
                    cmbMarca.setSelectedIndex(i + 1);
                    break;
                }
            }

            // Carrega modelos da marca e seleciona o modelo correto
            carregarModelosDaMarca(modelo.getIdMarca());
            for (int i = 0; i < modelosDisponiveis.size(); i++) {
                if (modelosDisponiveis.get(i).getId().equals(modelo.getId())) {
                    cmbModelo.setSelectedIndex(i + 1);
                    break;
                }
            }
        } catch (Exception ex) {
            // silencia: campos de marca/modelo ficam como estão
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

        modeloServicos = new DefaultTableModel(new String[]{"Serviço", "Tipo", "Valor"}, 0) {
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

        JButton btnAdd = criarBotaoNavy("+ Adicionar serviço", 160, 32);
        btnAdd.addActionListener(e -> adicionarServico());
        toolbar.add(btnAdd);

        card.add(scrollServ);
        card.add(toolbar);
        return card;
    }

    private JPanel criarCardPecas() {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        modeloPecas = new DefaultTableModel(new String[]{"Peça", "Qtd", "Valor Unit.", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JScrollPane scrollPec = new JScrollPane(criarTabela(modeloPecas));
        scrollPec.setBorder(null);
        scrollPec.getViewport().setBackground(Color.WHITE);
        scrollPec.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPec.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        toolbar.setOpaque(false);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        JButton btnAdd = criarBotaoNavy("+ Adicionar peça", 150, 32);
        btnAdd.addActionListener(e -> adicionarPeca());
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

        JButton btnCancelar = criarBotaoOutline("Cancelar", 110, 38);
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        JButton btnSalvar = criarBotaoGold("Salvar OS", 140, 38);
        btnSalvar.addActionListener(e -> salvarOS());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);

        p.add(totalPanel, BorderLayout.WEST);
        p.add(btnPanel,   BorderLayout.EAST);
        return p;
    }

    private void salvarOS() {
        try {
            int idxCliente = cmbCliente.getSelectedIndex();
            if (idxCliente <= 0 || clientesDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ClienteModel cliente = clientesDisponiveis.get(idxCliente - 1);

            if (cmbPlaca.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione a placa do veículo.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Normaliza: remove máscara antes de comparar/salvar
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
                    "Veículo com a placa informada não encontrado no cadastro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
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

            // Salva placa SEM máscara (sem traço) no banco
            os.setPlacaVeiculo(veiculoEncontrado.getPlaca());
            os.setNomeCliente(cliente.getNomeCompleto());

            OrdemServicoController osc = ContextoAplicacao.getBean(OrdemServicoController.class);
            osc.insert(os);

            JOptionPane.showMessageDialog(this, "OS salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            frame.mostrarTela(MainFrame.TELA_LISTA_OS);

        } catch (FieldValidationException | RuleValidationException valEx) {
            JOptionPane.showMessageDialog(this, valEx.getMessage(), "Erro de validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar OS: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    /**
     * Dialog de adicionar serviço:
     * - Somente serviços cadastrados (sem "digitar manualmente")
     * - Valor com máscara monetária R$ 1.000,00
     */
    private void adicionarServico() {
        List<String> nomes = new ArrayList<>();
        if (servicosInternos.isEmpty() && servicosExternos.isEmpty()) {
            nomes.add("Nenhum serviço cadastrado");
        }
        for (ServicoInternoModel s : servicosInternos) nomes.add("[INT] " + s.getDescricao());
        for (ServicoExternoModel s : servicosExternos) nomes.add("[EXT] " + s.getDescricao());

        JComboBox<String> cmbServico = new JComboBox<>(nomes.toArray(new String[0]));
        cmbServico.setFont(MainFrame.FONT_NORMAL);

        JTextField txtValor = new JTextField("0,00", 10);
        aplicarMascaraMonetaria(txtValor);

        // Ao selecionar serviço, preenche o valor automaticamente
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
            // Formata e exibe no campo de valor
            txtValor.setText(String.format("%.2f", valor).replace(".", ","));
        });

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Selecione o serviço:")); form.add(cmbServico);
        form.add(new JLabel("Valor (R$):"));          form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Serviço",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

            if (servicosInternos.isEmpty() && servicosExternos.isEmpty()) return;

            int idx = cmbServico.getSelectedIndex();
            String nome;
            String tipo;
            if (idx < servicosInternos.size()) {
                nome = "[INT] " + servicosInternos.get(idx).getDescricao();
                tipo = "Interno";
            } else {
                int idxExt = idx - servicosInternos.size();
                nome = "[EXT] " + servicosExternos.get(idxExt).getDescricao();
                tipo = "Externo";
            }

            double valorDouble = parseMoeda(txtValor.getText());
            String valorStr = FMT_MOEDA.format(valorDouble);
            modeloServicos.addRow(new Object[]{ nome, tipo, valorStr });
            recalcularTotal();
        }
    }

    /**
     * Dialog de adicionar peça:
     * - DropDown com peças cadastradas no banco
     * - Valor com máscara monetária
     * - Quantidade somente inteiros
     */
    private void adicionarPeca() {
        // ── Combo de peças cadastradas ──
        List<String> nomesPecas = new ArrayList<>();
        if (pecasDisponiveis.isEmpty()) {
            nomesPecas.add("Nenhuma peça cadastrada");
        }
        for (PecaModel p : pecasDisponiveis)
            nomesPecas.add(p.getMarca() + " — " + p.getModelo() + " (" + p.getAnoVeiculo() + ")");

        JComboBox<String> cmbPeca = new JComboBox<>(nomesPecas.toArray(new String[0]));
        cmbPeca.setFont(MainFrame.FONT_NORMAL);

        // ── Campo quantidade — somente inteiros ──
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

        // ── Campo valor com máscara monetária ──
        JTextField txtValor = new JTextField("0,00", 10);
        aplicarMascaraMonetaria(txtValor);

        // Ao selecionar peça, preenche preço unitário automaticamente
        cmbPeca.addActionListener(e -> {
            int idx = cmbPeca.getSelectedIndex();
            if (!pecasDisponiveis.isEmpty() && idx < pecasDisponiveis.size()) {
                double preco = pecasDisponiveis.get(idx).getPrecoUnitario();
                txtValor.setText(String.format("%.2f", preco).replace(".", ","));
            }
        });

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Selecione a peça:")); form.add(cmbPeca);
        form.add(new JLabel("Quantidade:"));       form.add(txtQtd);
        form.add(new JLabel("Valor Unit. (R$):")); form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Peça",
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

    /**
     * Aplica máscara de moeda pt-BR num JTextField.
     * Aceita somente dígitos; formata ao sair do campo como 1.000,00.
     */
    private void aplicarMascaraMonetaria(JTextField campo) {
        // Permite apenas dígitos e vírgula durante a digitação;
        // ao perder foco, formata no padrão 1.000,00
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
                // Formata: 1000.50 → "1.000,50"
                campo.setText(String.format("%,.2f", v).replace(".", "#").replace(",", ".").replace("#", ","));
            }
        });
    }

    /** Converte "1.000,50" ou "1000,50" ou "1000.50" → double */
    private double parseMoeda(String texto) {
        if (texto == null || texto.isBlank()) return 0;
        // Remove tudo que não é dígito ou vírgula; troca vírgula por ponto
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

    /** ABC1234 ou ABC1D23 → ABC-1234 / ABC-1D23 */
    private static String formatarPlaca(String placa) {
        if (placa == null || placa.length() < 7) return placa != null ? placa : "—";
        String p = placa.toUpperCase().replace("-", "").trim();
        if (p.length() == 7) return p.substring(0, 3) + "-" + p.substring(3);
        return placa;
    }

    /** "JOÃO DA SILVA" → "João da Silva" */
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
        JButton btn = new JButton("← Voltar") {
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