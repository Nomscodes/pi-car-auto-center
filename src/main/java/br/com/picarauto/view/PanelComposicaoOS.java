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
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Imports do backend para carregar dados e salvar a OS no banco
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.OrdemServicoController;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.controller.ColaboradorController;
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.ServicoInternoController;
import br.com.picarauto.controller.ServicoExternoController;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import java.util.ArrayList;
import java.util.List;

public class PanelComposicaoOS extends JPanel {

    private final MainFrame frame;

    // Listas carregadas do banco para popular os combos e identificar ids ao salvar
    private List<ClienteModel>        clientesDisponiveis   = new ArrayList<>();
    private List<ColaboradorModel>    colaboradoresDisponiveis = new ArrayList<>();
    private List<VeiculoModel>        veiculosDisponiveis   = new ArrayList<>();
    private List<ServicoInternoModel> servicosInternos      = new ArrayList<>();
    private List<ServicoExternoModel> servicosExternos      = new ArrayList<>();

    private static final String[] MARCAS = {
        "Selecione...", "Chevrolet", "Volkswagen", "Hyundai", "Toyota", "Ford", "Fiat", "Honda"
    };
    private static final String[][] MODELOS = {
        {},
        {"Onix", "Tracker", "Cruze", "S10", "Spin", "Montana"},
        {"Gol", "Polo", "T-Cross", "Virtus", "Nivus", "Amarok"},
        {"HB20", "Creta", "Tucson", "Santa Fe", "Elantra"},
        {"Corolla", "Hilux", "SW4", "Yaris", "RAV4"},
        {"Ka", "EcoSport", "Ranger", "Territory", "Bronco"},
        {"Argo", "Pulse", "Cronos", "Toro", "Strada", "Mobi"},
        {"Civic", "HR-V", "City", "Fit", "CR-V"},
    };

    private JComboBox<String> cmbCliente, cmbColaborador, cmbMarca, cmbModelo, cmbStatus;
    private JTextField        txtPlaca, txtData;
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

    // Carrega clientes, colaboradores, veículos e serviços do banco e popula os combos.
    // Chamado pelo MainFrame ao navegar para TELA_COMPOSICAO.
    public void carregarDados() {
        carregarClientes();
        carregarColaboradores();
        carregarServicos();
        limparFormulario();
    }

    // Busca clientes ativos no banco e preenche o combo de clientes
    private void carregarClientes() {
        try {
            ClienteController cc = ContextoAplicacao.getBean(ClienteController.class);
            clientesDisponiveis = cc.findAll();
            cmbCliente.removeAllItems();
            cmbCliente.addItem("Selecione...");
            for (ClienteModel c : clientesDisponiveis)
                cmbCliente.addItem(c.getNomeCompleto());
        } catch (Exception ex) {
            cmbCliente.removeAllItems();
            cmbCliente.addItem("Erro ao carregar");
        }
    }

    // Busca colaboradores ativos no banco e preenche o combo de colaboradores
    private void carregarColaboradores() {
        try {
            ColaboradorController cc = ContextoAplicacao.getBean(ColaboradorController.class);
            colaboradoresDisponiveis = cc.findAll();
            cmbColaborador.removeAllItems();
            cmbColaborador.addItem("Selecione...");
            for (ColaboradorModel c : colaboradoresDisponiveis)
                cmbColaborador.addItem(c.getNomeCompleto());
        } catch (Exception ex) {
            cmbColaborador.removeAllItems();
            cmbColaborador.addItem("Erro ao carregar");
        }
    }

    // Busca serviços internos e externos ativos no banco para uso no dialog de adição
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

    // Limpa os campos do formulário para uma nova OS
    private void limparFormulario() {
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtPlaca.setText("");
        if (txtObs != null) txtObs.setText("");
        modeloServicos.setRowCount(0);
        modeloPecas.setRowCount(0);
        lblTotal.setText("R$ 0,00");
        cmbMarca.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    private JPanel criarCardDados() {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Combos serão populados com dados do banco via carregarDados()
        cmbCliente     = criarCombo(new String[]{"Selecione..."});
        cmbColaborador = criarCombo(new String[]{"Selecione..."});
        cmbMarca       = criarCombo(MARCAS);
        cmbModelo      = criarCombo(new String[]{"Selecione primeiro a marca..."});
        cmbModelo.setEnabled(false);

        // Status segue o enum do banco: ORCAMENTO, EXECUCAO, PAGAMENTO, FINALIZADO
        cmbStatus = criarCombo(new String[]{
            "ORCAMENTO", "EXECUCAO", "PAGAMENTO", "FINALIZADO"
        });

        txtPlaca = criarCampo();
        txtData  = criarCampo();
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Ao selecionar uma marca, filtra os modelos disponíveis no combo
        cmbMarca.addActionListener(e -> {
            int idx = cmbMarca.getSelectedIndex();
            cmbModelo.removeAllItems();
            if (idx > 0 && idx < MODELOS.length) {
                cmbModelo.setEnabled(true);
                cmbModelo.addItem("Selecione o modelo...");
                for (String m : MODELOS[idx]) cmbModelo.addItem(m);
            } else {
                cmbModelo.setEnabled(false);
                cmbModelo.addItem("Selecione primeiro a marca...");
            }
        });

        // Ao selecionar um cliente, carrega os veículos vinculados a ele no combo de placa
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
        row3.add(criarGrupoCampo("Placa do veículo",  txtPlaca));
        row3.add(criarGrupoCampo("Data Abertura",      txtData));
        row3.add(criarGrupoCombo("Status",             cmbStatus));

        card.add(row1);
        card.add(Box.createVerticalStrut(12));
        card.add(row2);
        card.add(Box.createVerticalStrut(12));
        card.add(row3);
        return card;
    }

    // Busca os veículos vinculados ao cliente selecionado e preenche o campo de placa
    private void carregarVeiculosDoCliente(Long idCliente) {
        try {
            VeiculoController vc = ContextoAplicacao.getBean(VeiculoController.class);
            veiculosDisponiveis = vc.findAll().stream()
                .filter(v -> idCliente.equals(v.getIdCliente()))
                .toList();
            // Se o cliente tiver apenas um veículo, preenche automaticamente
            if (veiculosDisponiveis.size() == 1) {
                txtPlaca.setText(veiculosDisponiveis.get(0).getPlaca());
            } else {
                txtPlaca.setText("");
            }
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
        // Coleta os dados do formulário, monta o OrdemServicoModel e salva no banco
        btnSalvar.addActionListener(e -> salvarOS());

        btnPanel.add(btnCancelar);
        btnPanel.add(btnSalvar);

        p.add(totalPanel, BorderLayout.WEST);
        p.add(btnPanel,   BorderLayout.EAST);
        return p;
    }

    // Valida os campos obrigatórios, monta o OrdemServicoModel e chama o controller para salvar
    private void salvarOS() {
        try {
            // Valida seleção de cliente
            int idxCliente = cmbCliente.getSelectedIndex();
            if (idxCliente <= 0 || clientesDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ClienteModel cliente = clientesDisponiveis.get(idxCliente - 1);

            // Busca o veículo pela placa digitada — obrigatório para montar a OS
            String placaDigitada = txtPlaca.getText().replace("-", "").toUpperCase().trim();
            if (placaDigitada.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a placa do veículo.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

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

            // Monta o OrdemServicoModel com os dados do formulário
            OrdemServicoModel os = new OrdemServicoModel();
            os.setIdVeiculo(veiculoEncontrado.getId());
            os.setDataAbertura(LocalDate.now());
            os.setObservacoes(txtObs != null ? txtObs.getText().trim() : "");

            // Mapeia o status selecionado no combo para o enum do banco
            String statusSel = (String) cmbStatus.getSelectedItem();
            os.setStatus(OrdemServicoModel.StatusOrdemServico.valueOf(statusSel));

            // Calcula o valor total com base nos serviços e peças adicionados na tela
            double total = 0;
            for (int i = 0; i < modeloServicos.getRowCount(); i++) {
                try {
                    total += Double.parseDouble(modeloServicos.getValueAt(i, 2).toString()
                        .replace("R$", "").replace(".", "").replace(",", ".").trim());
                } catch (NumberFormatException ignored) {}
            }
            for (int i = 0; i < modeloPecas.getRowCount(); i++) {
                try {
                    total += Double.parseDouble(modeloPecas.getValueAt(i, 3).toString()
                        .replace("R$", "").replace(".", "").replace(",", ".").trim());
                } catch (NumberFormatException ignored) {}
            }
            os.setValorTotal(total > 0 ? total : null);

            // Popula os campos @Transient para enfileiramento na FilaOS
            os.setPlacaVeiculo(veiculoEncontrado.getPlaca());
            os.setNomeCliente(cliente.getNomeCompleto());

            // Salva a OS no banco via OrdemServicoController
            OrdemServicoController osc = ContextoAplicacao.getBean(OrdemServicoController.class);
            osc.insert(os);

            JOptionPane.showMessageDialog(this, "OS salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            frame.mostrarTela(MainFrame.TELA_LISTA_OS);

        } catch (FieldValidationException | RuleValidationException valEx) {
            // Exibe a mensagem de validação do backend
            JOptionPane.showMessageDialog(this, valEx.getMessage(), "Erro de validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar OS: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    // Abre dialog para adicionar um serviço à OS.
    // Se houver serviços cadastrados no banco, exibe um combo para seleção.
    // Caso contrário, permite digitação manual.
    private void adicionarServico() {
        List<String> nomes = new ArrayList<>();
        nomes.add("-- Digitar manualmente --");
        for (ServicoInternoModel s : servicosInternos) nomes.add("[INT] " + s.getDescricao());
        for (ServicoExternoModel s : servicosExternos) nomes.add("[EXT] " + s.getDescricao());

        JComboBox<String> cmbServico = new JComboBox<>(nomes.toArray(new String[0]));
        JTextField txtNomeManual = criarCampo();
        JTextField txtValor      = new JTextField("0,00", 10);
        txtNomeManual.setEnabled(false);

        // Ao selecionar "Digitar manualmente", habilita o campo de texto
        cmbServico.addActionListener(e -> {
            boolean manual = cmbServico.getSelectedIndex() == 0;
            txtNomeManual.setEnabled(manual);
            if (!manual) {
                int idx = cmbServico.getSelectedIndex() - 1;
                double valor = 0;
                String tipo = "Interno";
                if (idx < servicosInternos.size()) {
                    valor = servicosInternos.get(idx).getValorCobrado();
                } else {
                    valor = servicosExternos.get(idx - servicosInternos.size()).getValorCobrado();
                    tipo = "Externo";
                }
                txtValor.setText(String.format("%.2f", valor).replace(".", ","));
            }
        });

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("Serviço do catálogo:")); form.add(cmbServico);
        form.add(new JLabel("Nome manual:"));         form.add(txtNomeManual);
        form.add(new JLabel("Tipo:"));
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Interno", "Externo"});
        form.add(cmbTipo);
        form.add(new JLabel("Valor (R$):"));          form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Serviço",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

            String nome;
            String tipo;
            if (cmbServico.getSelectedIndex() == 0) {
                // Modo manual: usa o nome digitado e o tipo selecionado
                nome = txtNomeManual.getText().trim();
                tipo = (String) cmbTipo.getSelectedItem();
            } else {
                // Modo catálogo: usa o nome do serviço selecionado
                nome = (String) cmbServico.getSelectedItem();
                int idx = cmbServico.getSelectedIndex() - 1;
                tipo = idx < servicosInternos.size() ? "Interno" : "Externo";
            }

            if (!nome.isEmpty()) {
                String valorStr = "R$ " + txtValor.getText().trim().replace(",", ".");
                modeloServicos.addRow(new Object[]{ nome, tipo, valorStr });
                recalcularTotal();
            }
        }
    }

    // Abre dialog para adicionar uma peça à OS com quantidade e valor unitário
    private void adicionarPeca() {
        JTextField txtNome  = new JTextField(20);
        JTextField txtQtd   = new JTextField("1", 5);
        JTextField txtValor = new JTextField("0,00", 10);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Peça:"));         form.add(txtNome);
        form.add(new JLabel("Quantidade:"));   form.add(txtQtd);
        form.add(new JLabel("Valor Unit. (R$):")); form.add(txtValor);

        if (JOptionPane.showConfirmDialog(this, form, "Adicionar Peça",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String nome = txtNome.getText().trim();
            if (!nome.isEmpty()) {
                int qtd = 1;
                try { qtd = Integer.parseInt(txtQtd.getText().trim()); } catch (NumberFormatException ignored) {}
                double valor = 0;
                try {
                    valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
                } catch (NumberFormatException ignored) {}
                String valorUnitStr = String.format("R$ %.2f", valor).replace(".", ",");
                String totalStr     = String.format("R$ %.2f", valor * qtd).replace(".", ",");
                modeloPecas.addRow(new Object[]{ nome, qtd, valorUnitStr, totalStr });
                recalcularTotal();
            }
        }
    }

    // Recalcula o total somando todos os serviços e peças adicionados na tela
    private void recalcularTotal() {
        double total = 0;
        for (int i = 0; i < modeloServicos.getRowCount(); i++) {
            try {
                total += Double.parseDouble(modeloServicos.getValueAt(i, 2).toString()
                    .replace("R$", "").replace(".", "").replace(",", ".").trim());
            } catch (NumberFormatException ignored) {}
        }
        for (int i = 0; i < modeloPecas.getRowCount(); i++) {
            try {
                total += Double.parseDouble(modeloPecas.getValueAt(i, 3).toString()
                    .replace("R$", "").replace(".", "").replace(",", ".").trim());
            } catch (NumberFormatException ignored) {}
        }
        lblTotal.setText(String.format("R$ %.2f", total).replace(".", ","));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
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