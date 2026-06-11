package br.com.picarauto.view;

/**
 * Lista de clientes — busca, chips de filtro PF/PJ e tabela estilizada.
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

public class PanelListaClientes extends JPanel {

    private final MainFrame frame;
    private JTextField    txtBusca;
    private JTable        tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;
    private String        filtroTipo = "Todos";

    private static final String[] COLUNAS = {"Nome", "Tipo", "CPF / CNPJ", "Telefone", "Veículos", ""};

    private static final Object[][] DADOS_MOCK = {
        {"Marcos Silva",     "PF", "123.456.789-00", "(47) 99111-2222", "2"},
        {"Ana Pereira",      "PF", "987.654.321-00", "(47) 98222-3333", "1"},
        {"Tech Ltda",        "PJ", "12.345.678/0001-99", "(47) 3300-4444", "4"},
        {"Roberto Leal",     "PF", "321.654.987-00", "(47) 99444-5555", "1"},
        {"Construções SA",   "PJ", "98.765.432/0001-11", "(47) 3311-6666", "6"},
        {"Carla Moura",      "PF", "456.123.789-00", "(47) 97555-7777", "1"},
        {"Fábio Nunes",     "PF", "789.321.456-00", "(47) 99666-8888", "3"},
    };

    public PanelListaClientes(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_LISTA_CLIENTES), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Clientes");
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
            @Override public void insertUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        JButton btnNovo = criarBotaoNavy("Novo cliente", 120, 34);
        btnNovo.addActionListener(e -> abrirFormNovoCliente());

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);

        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        linha2.setOpaque(false);
        for (String tipo : new String[]{"Todos", "PF", "PJ"}) {
            linha2.add(criarChipFiltro(tipo));
        }

        cmbOrdenar = new JComboBox<>(new String[]{"Padrão", "A-Z (Nome)", "Z-A (Nome)"});
        cmbOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbOrdenar.setBackground(Color.WHITE);
        cmbOrdenar.setPreferredSize(new Dimension(160, 28));
        cmbOrdenar.addActionListener(e -> aplicarFiltros());
        linha2.add(cmbOrdenar);

        JPanel barra = new JPanel(new BorderLayout(0, 6));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        barra.add(painelBusca, BorderLayout.NORTH);
        barra.add(linha2, BorderLayout.SOUTH);
        return barra;
    }

    private JPanel criarChipFiltro(String tipo) {
        JPanel chip = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(58, 28));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        filtroTipo = tipo;
                        repaint();
                        getParent().repaint();
                        aplicarFiltros();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = tipo.equals(filtroTipo);
                g2.setColor(ativo ? MainFrame.COR_NAVY : (hover ? MainFrame.COR_BORDER : new Color(0xe0dbd0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(ativo ? Color.WHITE : new Color(0x555555));
                g2.setFont(new Font("Segoe UI", ativo ? Font.BOLD : Font.PLAIN, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(tipo, (getWidth() - fm.stringWidth(tipo)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        return chip;
    }

    private void aplicarFiltros() {
        if (sorter == null) return;
        String sel = cmbOrdenar == null ? "Padrão" : (String) cmbOrdenar.getSelectedItem();
        if (sel == null) sel = "Padrão";
        String txt = txtBusca.getText().trim();
        boolean hasText = !txt.isEmpty() && !"Pesquisar...".equals(txt);

        sorter.setSortKeys(java.util.Collections.emptyList());
        if ("A-Z (Nome)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Z-A (Nome)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList<>();
        if (hasText) filtros.add(RowFilter.regexFilter("(?i)" + txt));
        if ("PF".equals(filtroTipo))  filtros.add(RowFilter.regexFilter("^PF$", 1));
        else if ("PJ".equals(filtroTipo)) filtros.add(RowFilter.regexFilter("^PJ$", 1));
        sorter.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : DADOS_MOCK) modelo.addRow(row);

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

        tabela.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(1).setCellRenderer(new TipoPillRenderer());
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new EditarRenderer());

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
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

    // ── Dialog Novo Cliente ───────────────────────────────────────────────────
    private void abrirFormNovoCliente() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Novo Cliente", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 330);
        dialog.setLocationRelativeTo(this);

        boolean[] modoEmpresa = {false};

        JTextField txtNomePF = criarCampoDlg();
        JTextField txtCPF    = criarCampoDlg();
        JTextField txtTelPF  = criarCampoDlg();

        JTextField txtRazao  = criarCampoDlg();
        JTextField txtCNPJ   = criarCampoDlg();
        JTextField txtTelPJ  = criarCampoDlg();

        JPanel gridPF = new JPanel(new GridLayout(2, 2, 14, 10));
        gridPF.setOpaque(false);
        gridPF.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridPF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        gridPF.add(criarGrupoDlg("Nome completo", txtNomePF));
        gridPF.add(criarGrupoDlg("CPF",           txtCPF));
        gridPF.add(criarGrupoDlg("Telefone",      txtTelPF));
        gridPF.add(new JPanel() {{ setOpaque(false); }});

        JPanel gridPJ = new JPanel(new GridLayout(2, 2, 14, 10));
        gridPJ.setOpaque(false);
        gridPJ.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridPJ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        gridPJ.add(criarGrupoDlg("Razão Social", txtRazao));
        gridPJ.add(criarGrupoDlg("CNPJ",         txtCNPJ));
        gridPJ.add(criarGrupoDlg("Telefone",     txtTelPJ));
        gridPJ.add(new JPanel() {{ setOpaque(false); }});

        JPanel camposCard = new JPanel(new CardLayout());
        camposCard.setOpaque(false);
        camposCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        camposCard.add(gridPF, "PF");
        camposCard.add(gridPJ, "PJ");

        JButton btnPF = criarBotaoToggleDlg("Pessoa Física",   modoEmpresa);
        JButton btnPJ = criarBotaoToggleDlg("Pessoa Jurídica", modoEmpresa);
        btnPF.addActionListener(e -> { modoEmpresa[0] = false; ((CardLayout)camposCard.getLayout()).show(camposCard,"PF"); btnPF.repaint(); btnPJ.repaint(); });
        btnPJ.addActionListener(e -> { modoEmpresa[0] = true;  ((CardLayout)camposCard.getLayout()).show(camposCard,"PJ"); btnPF.repaint(); btnPJ.repaint(); });

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        togglePanel.add(btnPF);
        togglePanel.add(btnPJ);

        JButton btnCanc = criarBotaoOutlineDlg("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGoldDlg("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            if (!modoEmpresa[0]) {
                String nome = txtNomePF.getText().trim();
                if (!nome.isEmpty())
                    modelo.addRow(new Object[]{nome, "PF", txtCPF.getText().trim(), txtTelPF.getText().trim(), "0"});
            } else {
                String razao = txtRazao.getText().trim();
                if (!razao.isEmpty())
                    modelo.addRow(new Object[]{razao, "PJ", txtCNPJ.getText().trim(), txtTelPJ.getText().trim(), "0"});
            }
            dialog.dispose();
        });

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(togglePanel);
        form.add(Box.createVerticalStrut(14));
        form.add(camposCard);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    private JPanel criarGrupoDlg(String label, JTextField campo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        g.add(lbl); g.add(Box.createVerticalStrut(4)); g.add(campo);
        return g;
    }

    private JTextField criarCampoDlg() {
        JTextField f = new JTextField();
        f.setFont(MainFrame.FONT_NORMAL);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34));
        return f;
    }

    private JButton criarBotaoToggleDlg(String texto, boolean[] modo) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (texto.contains("Física") && !modo[0]) || (texto.contains("Jurídica") && modo[0]);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(ativo ? Color.WHITE : MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
    }

    private JButton criarBotaoGoldDlg(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoOutlineDlg(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setForeground(MainFrame.COR_NAVY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    // ── Renderers ─────────────────────────────────────────────────────────────
    static class TipoPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            String tipo = v == null ? "" : v.toString();
            boolean pj = "PJ".equals(tipo);
            JLabel lbl = new JLabel(tipo, SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(pj ? new Color(0xFAEEDA) : new Color(0xE1F5EE));
                    g2.fillRoundRect(4, (getHeight() - 20) / 2, getWidth() - 8, 20, 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(pj ? new Color(0x854F0B) : new Color(0x0F6E56));
            lbl.setOpaque(false);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }

    static class EditarRenderer extends DefaultTableCellRenderer {
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
