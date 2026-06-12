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

// Imports do backend para carregar OS do banco
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.OrdemServicoController;
import br.com.picarauto.model.OrdemServicoModel;
import java.util.List;

public class PanelListaOS extends JPanel {

    private final MainFrame frame;
    private JTextField   txtBusca;
    private JTable       tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    // Guarda as OS carregadas do banco na mesma ordem das linhas da tabela
    private List<OrdemServicoModel> osAtuais;

    private static final String[] COLUNAS = {"Nº OS", "Cliente", "Veículo", "Data", "Status", "Valor", ""};

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

    // ── Topbar ────────────────────────────────────────────────────────────────
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

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JPanel criarConteudo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainFrame.COR_CREAM);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
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
                case "Cliente A → Z"    -> sorter.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                case "Cliente Z → A"    -> sorter.setSortKeys(List.of(new RowSorter.SortKey(1, SortOrder.DESCENDING)));
                case "Nº OS crescente"  -> sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                case "Nº OS decrescente"-> sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
                default                 -> sorter.setSortKeys(java.util.Collections.emptyList());
            }
        });

        JButton btnNova = criarBotaoNavy("Nova OS", 110, 34);
        // Navega para seleção de marca com modoNovaOS=true em vez de abrir dialog local
        btnNova.addActionListener(e -> {
            PanelSelecaoMarca.modoNovaOS = true;
            frame.mostrarTela(MainFrame.TELA_MARCA);
        });

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);
        direita.add(cmbOrdem);
        direita.add(btnNova);

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(direita, BorderLayout.EAST);
        return painelBusca;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // A tabela começa vazia — o carregamento do banco só ocorre quando o usuário
        // navegar para essa tela via MainFrame.mostrarTela(TELA_LISTA_OS),
        // que chama carregarOS(). Isso evita o erro "contexto não inicializado pelo Spring"
        // que ocorre quando o painel é construído antes do Spring terminar de subir.

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

        // Detecta clique na coluna "Ver OS" e navega para PanelComposicaoOS
        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewRow = tabela.rowAtPoint(e.getPoint());
                int viewCol = tabela.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol != 6) return;
                int modelRow = tabela.convertRowIndexToModel(viewRow);
                if (osAtuais == null || modelRow >= osAtuais.size()) return;
                OrdemServicoModel osSelecionada = osAtuais.get(modelRow);
                frame.mostrarTela(MainFrame.TELA_COMPOSICAO);
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // Busca todas as OS ativas do banco com placa e nome do cliente já populados,
    // e preenche a tabela. Chamado pelo MainFrame ao navegar para TELA_LISTA_OS.
    public void carregarOS() {
        modelo.setRowCount(0);
        try {
            OrdemServicoController controller = ContextoAplicacao.getBean(OrdemServicoController.class);
            osAtuais = controller.findAllEnriquecido();

            for (OrdemServicoModel os : osAtuais) {
                String numOS   = "#" + String.format("%04d", os.getId());
                String cliente = os.getNomeCliente()  != null ? os.getNomeCliente()  : "-";
                String veiculo = os.getPlacaVeiculo() != null ? os.getPlacaVeiculo() : "-";
                String data    = os.getDataAbertura() != null
                    ? os.getDataAbertura().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "-";
                String status  = os.getStatus() != null ? os.getStatus().name() : "-";
                String valor   = os.getValorTotal() != null
                    ? String.format("R$ %.2f", os.getValorTotal())
                    : "-";
                modelo.addRow(new Object[]{ numOS, cliente, veiculo, data, status, valor });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar OS: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
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

    // ── Renderers ─────────────────────────────────────────────────────────────
    static class StatusPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            String raw = v == null ? "" : v.toString();
            String label = switch (raw) {
                case "ORCAMENTO" -> "Orçamento";
                case "EXECUCAO"  -> "Execução";
                case "PAGAMENTO" -> "Pagamento";
                case "FINALIZADO"-> "Finalizado";
                default          -> raw;
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

    static class AcaoRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Ver OS", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}