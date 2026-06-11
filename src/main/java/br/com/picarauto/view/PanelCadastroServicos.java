package br.com.picarauto.view;

/**
 * Cadastro de serviços — abas Internos / Externos com busca e tabela.
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

public class PanelCadastroServicos extends JPanel {

    private final MainFrame frame;

    private boolean abaInterno = true;
    private JTextField txtBusca;
    private JTable     tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    private static final String[] COLUNAS = {"Nome", "Tipo", "Valor", ""};

    private static final Object[][] INTERNOS = {
        {"Troca de Óleo",              "Interno", "R$ 80,00"},
        {"Alinhamento",                "Interno", "R$ 60,00"},
        {"Balanceamento",              "Interno", "R$ 50,00"},
        {"Revisão Geral",              "Interno", "R$ 180,00"},
        {"Troca de Pastilhas",         "Interno", "R$ 120,00"},
    };

    private static final Object[][] EXTERNOS = {
        {"Funilaria",                  "Externo", "R$ 350,00"},
        {"Pintura Total",              "Externo", "R$ 800,00"},
        {"Retífica de Motor",          "Externo", "R$ 1.200,00"},
        {"Ar Condicionado",            "Externo", "R$ 220,00"},
    };

    public PanelCadastroServicos(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_SERVICOS), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Serviços");
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
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
        JPanel barra = new JPanel(new BorderLayout(0, 10));
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(0, 0, 12, 0));

        // Toggle Internos / Externos
        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);

        JButton[] btns = new JButton[2];
        btns[0] = criarBotaoAba("Internos");
        btns[1] = criarBotaoAba("Externos");

        btns[0].addActionListener(e -> { abaInterno = true;  recarregarTabela(); btns[0].repaint(); btns[1].repaint(); });
        btns[1].addActionListener(e -> { abaInterno = false; recarregarTabela(); btns[0].repaint(); btns[1].repaint(); });

        abas.add(btns[0]);
        abas.add(btns[1]);

        // Linha busca + botão
        JPanel linha2 = new JPanel(new BorderLayout(12, 0));
        linha2.setOpaque(false);

        txtBusca = new JTextField();
        txtBusca.setFont(MainFrame.FONT_NORMAL);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setToolTipText("Buscar serviço...");
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

        JButton btnNovo = criarBotaoNavy("Novo serviço", 120, 34);

        linha2.add(txtBusca, BorderLayout.CENTER);
        linha2.add(btnNovo,  BorderLayout.EAST);

        barra.add(abas,   BorderLayout.NORTH);
        barra.add(linha2, BorderLayout.SOUTH);
        return barra;
    }

    private JButton criarBotaoAba(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (texto.equals("Internos") && abaInterno)
                             || (texto.equals("Externos") && !abaInterno);
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
        btn.setPreferredSize(new Dimension(110, 34));
        return btn;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        recarregarTabela();

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

        tabela.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setCellRenderer(new TipoPillRenderer());
        tabela.getColumnModel().getColumn(3).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(3).setCellRenderer(new EditarRenderer());

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void recarregarTabela() {
        if (modelo == null) return;
        modelo.setRowCount(0);
        Object[][] dados = abaInterno ? INTERNOS : EXTERNOS;
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

    // ── Renderers ─────────────────────────────────────────────────────────────
    static class TipoPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            String tipo = v == null ? "" : v.toString();
            boolean interno = "Interno".equals(tipo);
            JLabel lbl = new JLabel(tipo, SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(interno ? new Color(0xE6F1FB) : new Color(0xFAEEDA));
                    g2.fillRoundRect(4, (getHeight() - 20) / 2, getWidth() - 8, 20, 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(interno ? new Color(0x185FA5) : new Color(0x854F0B));
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
