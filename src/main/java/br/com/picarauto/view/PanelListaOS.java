package br.com.picarauto.view;

/**
 * Lista de Ordens de Serviço — busca, ordenação e tabela com pills de status.
 * Layout: topbar (NORTH) + conteúdo (CENTER) + sidebar (EAST).
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.OrdemServicoController;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.util.OrdenadorPorData;
import br.com.picarauto.util.OrdenadorOS;

public class PanelListaOS extends JPanel {

    private final MainFrame frame;
    private JTextField   txtBusca;
    private JTable       tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    private List<OrdemServicoModel> osAtuais;

    private static final String[] COLUNAS = {"Nº OS", "Cliente", "Veículo", "Data", "Status", "Valor", ""};

    private static final NumberFormat FMT_MOEDA =
        NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PanelListaOS(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_LISTA_OS), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Ordens de Serviço");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        bar.add(lbl, BorderLayout.WEST);
        bar.add(criarUsuarioPanel(), BorderLayout.EAST);
        return bar;
    }

    private JPanel criarUsuarioPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 9));
        p.setOpaque(false);
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fillOval(0, 0, 30, 30);
                String car = new String(Character.toChars(0x1F697));
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(car, (30 - fm.stringWidth(car)) / 2, (30 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(30, 30));
        JLabel nome = new JLabel(MainFrame.getUsuarioLogado());
        nome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nome.setForeground(new Color(0xccddff));
        p.add(av); p.add(nome);
        return p;
    }

    private JPanel criarConteudo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainFrame.COR_CREAM);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
        // ── Linha 1: busca por texto + ordenação + Nova OS ──────────────────
        txtBusca = new JTextField();
        txtBusca.setPreferredSize(new Dimension(0, 36));
        txtBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setText("Pesquisar...");
        txtBusca.setForeground(Color.GRAY);
        txtBusca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("Pesquisar...".equals(txtBusca.getText())) {
                    txtBusca.setText(""); txtBusca.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBusca.getText().isEmpty()) {
                    txtBusca.setText("Pesquisar..."); txtBusca.setForeground(Color.GRAY);
                }
            }
        });
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
            void filtrar() {
                if (sorter == null) return;
                String txt = txtBusca.getText().trim();
                if ("Pesquisar...".equals(txt)) { sorter.setRowFilter(null); return; }
                sorter.setRowFilter(txt.isEmpty() ? null : RowFilter.regexFilter("(?i)" + txt));
            }
        });

        String[] opcoes = {
            "Ordenar por...",
            "Cliente A → Z", "Cliente Z → A",
            "Nº OS crescente", "Nº OS decrescente",
        };
        JComboBox<String> cmbOrdem = new JComboBox<>(opcoes);
        cmbOrdem.setFont(MainFrame.FONT_NORMAL);
        cmbOrdem.setBackground(Color.WHITE);
        cmbOrdem.setPreferredSize(new Dimension(200, 34));
        cmbOrdem.addActionListener(e -> {
            if (sorter == null) return;
            switch ((String) cmbOrdem.getSelectedItem()) {
                case "Cliente A → Z"     -> sorter.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                case "Cliente Z → A"     -> sorter.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.DESCENDING)));
                case "Nº OS crescente"   -> sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                case "Nº OS decrescente" -> sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
                default                  -> sorter.setSortKeys(java.util.Collections.emptyList());
            }
        });

        // BOTÃO TELA LISTA OS DE CRIAR NOVA OS
        JButton btnNova = criarBotaoNavy("Nova OS", 110, 34);
        btnNova.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_COMPOSICAO));

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);
        direita.add(cmbOrdem);
        direita.add(btnNova);

        JPanel linha1 = new JPanel(new BorderLayout(12, 0));
        linha1.setBackground(new Color(0xF5F0E6));
        linha1.add(txtBusca, BorderLayout.CENTER);
        linha1.add(direita, BorderLayout.EAST);

        // ── Linha 2: busca por data (Template Method) e por placa (TabelaHashOS) ──
        JTextField txtData = new JTextField();
        txtData.setPreferredSize(new Dimension(130, 32));
        txtData.setFont(MainFrame.FONT_NORMAL);
        txtData.setBackground(Color.WHITE);
        txtData.setToolTipText("dd/mm/aaaa");
        txtData.setText("dd/mm/aaaa");
        txtData.setForeground(Color.GRAY);
        txtData.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("dd/mm/aaaa".equals(txtData.getText())) {
                    txtData.setText(""); txtData.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtData.getText().isBlank()) {
                    txtData.setText("dd/mm/aaaa"); txtData.setForeground(Color.GRAY);
                }
            }
        });
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));

        // BOTÃO TELA LISTA OS DE BUSCAR OS POR DATA
        JButton btnBuscarData = criarBotaoNavy("Buscar por data", 140, 32);
        btnBuscarData.addActionListener(e -> buscarPorData(txtData.getText().trim()));

        JTextField txtPlaca = new JTextField();
        txtPlaca.setPreferredSize(new Dimension(120, 32));
        txtPlaca.setFont(MainFrame.FONT_NORMAL);
        txtPlaca.setBackground(Color.WHITE);
        txtPlaca.setToolTipText("Placa exata (ex: ABC1D23)");
        txtPlaca.setText("Placa exata...");
        txtPlaca.setForeground(Color.GRAY);
        txtPlaca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("Placa exata...".equals(txtPlaca.getText())) {
                    txtPlaca.setText(""); txtPlaca.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtPlaca.getText().isBlank()) {
                    txtPlaca.setText("Placa exata..."); txtPlaca.setForeground(Color.GRAY);
                }
            }
        });
        txtPlaca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));

        // BOTÃO TELA LISTA OS DE BUSCAR OS POR PLACA
        JButton btnBuscarPlaca = criarBotaoNavy("Buscar por placa", 145, 32);
        btnBuscarPlaca.addActionListener(e -> buscarPorPlaca(txtPlaca.getText().trim()));

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        linha2.setOpaque(false);
        linha2.add(new JLabel("Data:") {{ setFont(MainFrame.FONT_NORMAL); setForeground(new Color(0x444444)); }});
        linha2.add(txtData);
        linha2.add(btnBuscarData);
        linha2.add(Box.createHorizontalStrut(16));
        linha2.add(new JLabel("Placa:") {{ setFont(MainFrame.FONT_NORMAL); setForeground(new Color(0x444444)); }});
        linha2.add(txtPlaca);
        linha2.add(btnBuscarPlaca);

        JPanel barra = new JPanel(new BorderLayout(0, 8));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        barra.add(linha1, BorderLayout.NORTH);
        barra.add(linha2, BorderLayout.SOUTH);
        return barra;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(32);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(12, 0));
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionBackground(new Color(0xe8e3d8));
        tabela.setSelectionForeground(MainFrame.COR_NAVY);
        tabela.setFillsViewportHeight(true);
        tabela.setDefaultEditor(Object.class, null);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(MainFrame.COR_CREAM_ALT);
        header.setForeground(new Color(0x444444));
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainFrame.COR_BORDER));
        header.setReorderingAllowed(false);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(4).setCellRenderer(new StatusPillRenderer());
        tabela.getColumnModel().getColumn(6).setCellRenderer(new AcaoRenderer());

        // ALTERADO: coluna 6 agora abre o dialog de edição em vez de navegar para composição
        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewRow = tabela.rowAtPoint(e.getPoint());
                int viewCol = tabela.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol != 6) return;
                int modelRow = tabela.convertRowIndexToModel(viewRow);
                if (osAtuais == null || modelRow >= osAtuais.size()) return;
                OrdemServicoModel osSelecionada = osAtuais.get(modelRow);
                editarOS(osSelecionada); // NOVO: abre dialog de edição
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    /**
     * Exibe um resumo da OS selecionada num painel estilizado dentro de um JDialog.
     */
    private void mostrarResumoOS(OrdemServicoModel os) {
        // ── Monta os dados ──────────────────────────────────────────────────
        String numOS    = "#" + String.format("%04d", os.getId());
        String cliente  = os.getNomeCliente()  != null ? capitalizarNome(os.getNomeCliente()) : "—";
        String placa    = os.getPlacaVeiculo() != null ? formatarPlaca(os.getPlacaVeiculo())  : "—";
        String data     = os.getDataAbertura() != null
            ? os.getDataAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "—";
        String statusRaw = os.getStatus() != null ? os.getStatus().name() : "—";
        String statusLabel = switch (statusRaw) {
            case "ORCAMENTO"  -> "Orçamento";
            case "EXECUCAO"   -> "Execução";
            case "PAGAMENTO"  -> "Pagamento";
            case "FINALIZADO" -> "Finalizado";
            default           -> statusRaw;
        };
        String valor    = os.getValorTotal() != null ? FMT_MOEDA.format(os.getValorTotal()) : "R$ 0,00";
        String obs      = (os.getObservacoes() != null && !os.getObservacoes().isBlank())
            ? os.getObservacoes() : "Sem observações.";

        // ── Painel de resumo ────────────────────────────────────────────────
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        // Cabeçalho azul com nº da OS
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        JLabel lblNum = new JLabel("Ordem de Serviço  " + numOS);
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNum.setForeground(Color.WHITE);
        JLabel lblStatus = new JLabel(statusLabel);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(MainFrame.COR_GOLD);
        header.add(lblNum,    BorderLayout.WEST);
        header.add(lblStatus, BorderLayout.EAST);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(header);
        painel.add(Box.createVerticalStrut(14));

        // Grid de campos
        String[][] campos = {
            {"Cliente",        cliente},
            {"Placa",          placa},
            {"Data abertura",  data},
            {"Valor total",    valor},
        };
        JPanel grid = new JPanel(new java.awt.GridLayout(campos.length, 2, 8, 8));
        grid.setBackground(Color.WHITE);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String[] par : campos) {
            JLabel rotulo = new JLabel(par[0] + ":");
            rotulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rotulo.setForeground(new Color(0x555555));
            JLabel valor2 = new JLabel(par[1]);
            valor2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            valor2.setForeground(MainFrame.COR_NAVY);
            grid.add(rotulo);
            grid.add(valor2);
        }
        painel.add(grid);
        painel.add(Box.createVerticalStrut(12));

        // Observações
        JLabel lblObsRotulo = new JLabel("Observações:");
        lblObsRotulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblObsRotulo.setForeground(new Color(0x555555));
        lblObsRotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(lblObsRotulo);
        painel.add(Box.createVerticalStrut(4));

        JTextArea areaObs = new JTextArea(obs);
        areaObs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaObs.setForeground(new Color(0x333333));
        areaObs.setBackground(new Color(0xF5F0E6));
        areaObs.setEditable(false);
        areaObs.setLineWrap(true);
        areaObs.setWrapStyleWord(true);
        areaObs.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        areaObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollObs = new JScrollPane(areaObs);
        scrollObs.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER));
        scrollObs.setPreferredSize(new Dimension(420, 70));
        scrollObs.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.add(scrollObs);

        // ── Exibe o dialog ──────────────────────────────────────────────────
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Resumo da OS", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(painel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(480, 0));
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public void carregarOS() {
        modelo.setRowCount(0);
        try {
            OrdemServicoController controller = ContextoAplicacao.getBean(OrdemServicoController.class);
            osAtuais = controller.findAllEnriquecido();

            for (OrdemServicoModel os : osAtuais) {
                String numOS   = "#" + String.format("%04d", os.getId());
                String cliente = os.getNomeCliente() != null
                    ? capitalizarNome(os.getNomeCliente()) : "-";
                String veiculo = os.getPlacaVeiculo() != null
                    ? formatarPlaca(os.getPlacaVeiculo()) : "-";
                String data    = os.getDataAbertura() != null
                    ? os.getDataAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "-";
                String status  = os.getStatus() != null ? os.getStatus().name() : "-";
                String valor   = os.getValorTotal() != null
                    ? FMT_MOEDA.format(os.getValorTotal())
                    : "-";
                modelo.addRow(new Object[]{ numOS, cliente, veiculo, data, status, valor });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar OS: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== NOVO: edição de OS =====================

    /**
     * Abre um JDialog modal com os campos editáveis da OS:
     * status, data de fechamento, observações e valor total.
     * Chama controller.update() ao confirmar, garantindo persistência no banco.
     */
    private void editarOS(OrdemServicoModel os) {
        // Status
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{
            "ORCAMENTO", "EXECUCAO", "PAGAMENTO", "FINALIZADO"
        });
        cmbStatus.setFont(MainFrame.FONT_NORMAL);
        if (os.getStatus() != null) {
            cmbStatus.setSelectedItem(os.getStatus().name());
        }

        // NOVO: Data de fechamento
        JTextField txtDataFechamento = new JTextField();
        txtDataFechamento.setFont(MainFrame.FONT_NORMAL);
        txtDataFechamento.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        String dataFechAtual = os.getDataFechamento() != null
            ? os.getDataFechamento().format(FMT_DATA) : "";
        if (dataFechAtual.isEmpty()) {
            txtDataFechamento.setText("dd/mm/aaaa");
            txtDataFechamento.setForeground(Color.GRAY);
        } else {
            txtDataFechamento.setText(dataFechAtual);
            txtDataFechamento.setForeground(new Color(0x333333));
        }
        txtDataFechamento.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("dd/mm/aaaa".equals(txtDataFechamento.getText())) {
                    txtDataFechamento.setText("");
                    txtDataFechamento.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtDataFechamento.getText().isBlank()) {
                    txtDataFechamento.setText("dd/mm/aaaa");
                    txtDataFechamento.setForeground(Color.GRAY);
                }
            }
        });

        // NOVO: ao selecionar FINALIZADO, preenche automaticamente com a data de hoje (se vazio)
        cmbStatus.addActionListener(ev -> {
            if ("FINALIZADO".equals(cmbStatus.getSelectedItem())
                    && ("dd/mm/aaaa".equals(txtDataFechamento.getText())
                        || txtDataFechamento.getText().isBlank())) {
                txtDataFechamento.setText(LocalDate.now().format(FMT_DATA));
                txtDataFechamento.setForeground(new Color(0x333333));
            }
        });

        // Observações
        JTextArea txtObs = new JTextArea(os.getObservacoes() != null ? os.getObservacoes() : "", 4, 30);
        txtObs.setFont(MainFrame.FONT_NORMAL);
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        txtObs.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JScrollPane scrollObs = new JScrollPane(txtObs);
        scrollObs.setBorder(BorderFactory.createLineBorder(new Color(0xD0C9B8)));

        // Valor total
        JTextField txtValor = new JTextField(
            os.getValorTotal() != null
                ? String.format("%.2f", os.getValorTotal()).replace(".", ",")
                : "0,00", 12);
        txtValor.setFont(MainFrame.FONT_NORMAL);
        txtValor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        // Montagem do formulário
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 8));
        form.setBorder(new EmptyBorder(4, 0, 4, 0));
        form.setBackground(MainFrame.COR_CREAM);

        form.add(labelCinza("Status:"));
        form.add(cmbStatus);
        form.add(labelCinza("Data de Fechamento:"));
        form.add(txtDataFechamento);
        form.add(labelCinza("Observações:"));
        form.add(scrollObs);
        form.add(labelCinza("Valor total (R$):"));
        form.add(txtValor);

        // Botões do dialog
        // BOTÃO DIALOG EDITAR OS DE SALVAR ALTERAÇÕES
        JButton btnSalvar   = criarBotaoNavy("Salvar", 100, 34);
        // BOTÃO DIALOG EDITAR OS DE CANCELAR EDIÇÃO
        JButton btnCancelar = criarBotaoOutline("Cancelar", 100, 34);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rodape.setOpaque(false);
        rodape.add(btnCancelar);
        rodape.add(btnSalvar);

        JPanel conteudo = new JPanel(new BorderLayout(0, 12));
        conteudo.setBackground(MainFrame.COR_CREAM);
        conteudo.setBorder(new EmptyBorder(16, 16, 16, 16));
        conteudo.add(form,    BorderLayout.CENTER);
        conteudo.add(rodape,  BorderLayout.SOUTH);

        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(this),
            "Editar OS #" + String.format("%04d", os.getId()),
            java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 420); // ALTERADO: aumentado para acomodar o novo campo
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.add(conteudo);

        btnCancelar.addActionListener(ev -> dialog.dispose());

        btnSalvar.addActionListener(ev -> {
            try {
                String statusSel = (String) cmbStatus.getSelectedItem();
                String dataFechTexto = txtDataFechamento.getText().trim();

                // NOVO: validação da data de fechamento
                if ("FINALIZADO".equals(statusSel)) {
                    if (dataFechTexto.isBlank() || "dd/mm/aaaa".equals(dataFechTexto)) {
                        JOptionPane.showMessageDialog(dialog,
                            "Informe a data de fechamento (dd/mm/aaaa) para OS finalizada.",
                            "Campo obrigatório", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try {
                        os.setDataFechamento(LocalDate.parse(dataFechTexto, FMT_DATA));
                    } catch (DateTimeParseException dtpe) {
                        JOptionPane.showMessageDialog(dialog,
                            "Data de fechamento inválida. Use o formato dd/mm/aaaa.",
                            "Formato inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } else {
                    if (!dataFechTexto.isBlank() && !"dd/mm/aaaa".equals(dataFechTexto)) {
                        try {
                            os.setDataFechamento(LocalDate.parse(dataFechTexto, FMT_DATA));
                        } catch (DateTimeParseException dtpe) {
                            JOptionPane.showMessageDialog(dialog,
                                "Data de fechamento inválida. Use o formato dd/mm/aaaa.",
                                "Formato inválido", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        os.setDataFechamento(null);
                    }
                }

                // Aplica os campos editados no objeto OS
                os.setStatus(OrdemServicoModel.StatusOrdemServico.valueOf(statusSel));
                os.setObservacoes(txtObs.getText().trim());

                String valorTexto = txtValor.getText().trim().replace(",", ".");
                try {
                    double valor = Double.parseDouble(valorTexto);
                    os.setValorTotal(valor > 0 ? valor : null);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(dialog,
                        "Valor total inválido. Use o formato 0,00.",
                        "Erro de validação", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Persiste no banco via controller.update()
                OrdemServicoController controller = ContextoAplicacao.getBean(OrdemServicoController.class);
                controller.update(os);

                dialog.dispose();
                carregarOS(); // recarrega a tabela
                JOptionPane.showMessageDialog(this,
                    "OS #" + String.format("%04d", os.getId()) + " atualizada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao atualizar OS: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    /** Label auxiliar cinza para o formulário de edição. */
    private JLabel labelCinza(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        return lbl;
    }

    // ===================== FIM: edição de OS =====================

    // ===================== busca binária por data (Template Method) =====================

    private void buscarPorData(String textoData) {
        if (textoData.isBlank() || "dd/mm/aaaa".equals(textoData)) {
            JOptionPane.showMessageDialog(this,
                "Informe uma data no formato dd/mm/aaaa.",
                "Campo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate data;
        try {
            data = LocalDate.parse(textoData, FMT_DATA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                "Data inválida. Use o formato dd/mm/aaaa.",
                "Formato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            OrdemServicoController controller = ContextoAplicacao.getBean(OrdemServicoController.class);

            OrdenadorPorData ordenador = new OrdenadorPorData();
            List<OrdemServicoModel> ordenadas = ordenador.ordenar(controller.getFilaEspera());
            OrdemServicoModel resultado = ordenador.buscarBinariaPorData(ordenadas, data);

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                    "Nenhuma OS encontrada com abertura em " + textoData + ".",
                    "Não encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String msg = String.format(
                    "OS encontrada (Busca Binária):%n%n" +
                    "Nº OS:    #%04d%n" +
                    "Status:   %s%n" +
                    "Veículo:  %s%n" +
                    "Abertura: %s%n" +
                    "Total:    %s",
                    resultado.getId(),
                    resultado.getStatus() != null ? resultado.getStatus().name() : "-",
                    resultado.getPlacaVeiculo() != null ? formatarPlaca(resultado.getPlacaVeiculo()) : "-",
                    resultado.getDataAbertura() != null ? resultado.getDataAbertura().format(FMT_DATA) : "-",
                    resultado.getValorTotal() != null ? FMT_MOEDA.format(resultado.getValorTotal()) : "-"
                );
                JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro na busca por data: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== busca por placa exata (TabelaHashOS) =====================

    private void buscarPorPlaca(String placa) {
        if (placa.isBlank() || "Placa exata...".equals(placa)) {
            JOptionPane.showMessageDialog(this,
                "Informe a placa exata para busca.",
                "Campo vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            OrdemServicoController controller = ContextoAplicacao.getBean(OrdemServicoController.class);
            OrdemServicoModel resultado = controller.buscarPorPlacaExata(placa);

            if (resultado == null) {
                JOptionPane.showMessageDialog(this,
                    "Nenhuma OS encontrada para a placa \"" + placa.toUpperCase() + "\".",
                    "Não encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String msg = String.format(
                    "OS encontrada (Tabela Hash — O(1)):%n%n" +
                    "Nº OS:    #%04d%n" +
                    "Status:   %s%n" +
                    "Veículo:  %s%n" +
                    "Abertura: %s%n" +
                    "Total:    %s",
                    resultado.getId(),
                    resultado.getStatus() != null ? resultado.getStatus().name() : "-",
                    resultado.getPlacaVeiculo() != null ? formatarPlaca(resultado.getPlacaVeiculo()) : "-",
                    resultado.getDataAbertura() != null ? resultado.getDataAbertura().format(FMT_DATA) : "-",
                    resultado.getValorTotal() != null ? FMT_MOEDA.format(resultado.getValorTotal()) : "-"
                );
                JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro na busca por placa: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== helpers =====================

    private static String formatarPlaca(String placa) {
        if (placa == null || placa.length() < 7) return placa != null ? placa : "—";
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
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
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
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
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

    static class StatusPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            String raw = v == null ? "" : v.toString();
            String label = switch (raw) {
                case "ORCAMENTO"  -> "Orçamento";
                case "EXECUCAO"   -> "Execução";
                case "PAGAMENTO"  -> "Pagamento";
                case "FINALIZADO" -> "Finalizado";
                default           -> raw;
            };
            JLabel lbl = new JLabel(label, SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Color bg = switch (raw) {
                        case "ORCAMENTO"  -> new Color(0xE1F5EE);
                        case "EXECUCAO"   -> new Color(0xFAEEDA);
                        case "PAGAMENTO"  -> new Color(0xE6F1FB);
                        case "FINALIZADO" -> new Color(0xEAEAEA);
                        default           -> new Color(0xF0F0F0);
                    };
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(4, (getHeight() - 22) / 2, getWidth() - 8, 22, 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            Color fg = switch (raw) {
                case "ORCAMENTO"  -> new Color(0x0F6E56);
                case "EXECUCAO"   -> new Color(0x854F0B);
                case "PAGAMENTO"  -> new Color(0x185FA5);
                case "FINALIZADO" -> new Color(0x555555);
                default           -> Color.DARK_GRAY;
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(fg);
            lbl.setOpaque(false);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }

    // ALTERADO: texto de "Ver OS" para "Editar"
    static class AcaoRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Editar", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}