package br.com.picarauto.view;

/**
 * Gerenciamento de marcas e modelos — abas Marcas / Modelos com busca e tabela.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PanelMarcasModelos extends JPanel {

    private final MainFrame frame;

    private boolean abaMarcas = true;
    private JTextField txtBusca;
    private JTable     tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    private static final String[] COLUNAS_MARCAS  = {"Marca", "Modelos cadastrados", ""};
    private static final String[] COLUNAS_MODELOS = {"Modelo", "Marca", "Ano", ""};

    private static final Object[][] DADOS_MARCAS = {
        {"Chevrolet",  "Onix, Tracker, Cruze, S10, Spin"},
        {"Volkswagen", "Gol, Polo, T-Cross, Virtus, Nivus"},
        {"Fiat",       "Argo, Pulse, Cronos, Toro, Strada"},
        {"Ford",       "Ka, EcoSport, Ranger, Territory"},
        {"Toyota",     "Corolla, Hilux, SW4, Yaris, RAV4"},
        {"Honda",      "Civic, HR-V, City, Fit, CR-V"},
        {"Hyundai",    "HB20, Creta, Tucson, Santa Fe"},
        {"Renault",    "Kwid, Sandero, Logan, Duster"},
    };

    private static final Object[][] DADOS_MODELOS = {
        {"Onix",    "Chevrolet",  "2017–2026"},
        {"Tracker", "Chevrolet",  "2020–2026"},
        {"Gol",     "Volkswagen", "1980–2026"},
        {"Polo",    "Volkswagen", "2017–2026"},
        {"Argo",    "Fiat",       "2017–2026"},
        {"HB20",    "Hyundai",    "2012–2026"},
        {"Creta",   "Hyundai",    "2016–2026"},
        {"Kwid",    "Renault",    "2016–2026"},
    };

    public PanelMarcasModelos(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_MARCAS_MOD), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Marcas e Modelos");
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
                g2.setColor(MainFrame.COR_GOLD);
                g2.fillOval(0, 0, 30, 30);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String i = MainFrame.getUsuarioLogado().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(i, (30 - fm.stringWidth(i)) / 2, (30 + fm.getAscent() - fm.getDescent()) / 2);
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
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
        JPanel barra = new JPanel(new BorderLayout(0, 10));
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(0, 0, 14, 0));

        // Abas Marcas / Modelos
        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);

        JButton[] btns = { criarBotaoAba("Marcas"), criarBotaoAba("Modelos") };

        btns[0].addActionListener(e -> { abaMarcas = true;  recarregarTabela(); btns[0].repaint(); btns[1].repaint(); });
        btns[1].addActionListener(e -> { abaMarcas = false; recarregarTabela(); btns[0].repaint(); btns[1].repaint(); });

        abas.add(btns[0]);
        abas.add(btns[1]);

        // Busca + botão
        JPanel linha2 = new JPanel(new BorderLayout(12, 0));
        linha2.setOpaque(false);

        txtBusca = new JTextField();
        txtBusca.setFont(MainFrame.FONT_NORMAL);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setToolTipText("Buscar...");
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
            void filtrar() {
                if (sorter == null) return;
                String txt = txtBusca.getText().trim();
                sorter.setRowFilter(txt.isEmpty() ? null : RowFilter.regexFilter("(?i)" + txt));
            }
        });

        JButton btnNova = criarBotaoNavy("Nova marca", 120, 34);
        btnNova.addActionListener(e -> {
            String novaMarca = JOptionPane.showInputDialog(this,
                "Nome da nova marca:", "Nova marca", JOptionPane.PLAIN_MESSAGE);
            if (novaMarca != null && !novaMarca.isBlank()) {
                modelo.addRow(new Object[]{novaMarca.trim(), "—"});
            }
        });

        linha2.add(txtBusca, BorderLayout.CENTER);
        linha2.add(btnNova,  BorderLayout.EAST);

        barra.add(abas,   BorderLayout.NORTH);
        barra.add(linha2, BorderLayout.SOUTH);
        return barra;
    }

    private JButton criarBotaoAba(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (texto.equals("Marcas") && abaMarcas)
                             || (texto.equals("Modelos") && !abaMarcas);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(ativo ? Color.WHITE : MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 34));
        return btn;
    }

    private JScrollPane criarScrollTabela() {
        String[] cols = abaMarcas ? COLUNAS_MARCAS : COLUNAS_MODELOS;
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        recarregarTabela();

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(40);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
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

        int lastCol = tabela.getColumnCount() - 1;
        tabela.getColumnModel().getColumn(lastCol).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(lastCol).setCellRenderer(new EditarRenderer());

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void recarregarTabela() {
        if (modelo == null) return;
        modelo.setRowCount(0);
        Object[][] dados = abaMarcas ? DADOS_MARCAS : DADOS_MODELOS;
        for (Object[] row : dados) modelo.addRow(row);
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
