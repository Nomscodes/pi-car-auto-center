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
import java.awt.geom.RoundRectangle2D;

public class PanelListaOS extends JPanel {

    private final MainFrame frame;
    private JTextField   txtBusca;
    private JTable       tabela;
    private DefaultTableModel modelo;

    private static final String[] COLUNAS = {"Nº OS", "Cliente", "Veículo", "Data", "Status", "Valor", ""};

    private static final Object[][] DADOS_MOCK = {
        {"#0042", "Marcos Silva",   "Onix 2022",    "08/06/2026", "Concluída",  "R$ 620,00"},
        {"#0041", "Ana Pereira",    "HB20 2021",    "07/06/2026", "Andamento",  "R$ 380,00"},
        {"#0040", "Roberto Leal",   "Gol 2019",     "06/06/2026", "Aberta",     "R$ 150,00"},
        {"#0039", "Carla Moura",    "Pulse 2023",   "05/06/2026", "Concluída",  "R$ 940,00"},
        {"#0038", "Fábio Nunes",   "Kwid 2020",    "04/06/2026", "Aberta",     "R$ 210,00"},
        {"#0037", "Juliana Costa",  "HB20 2022",    "03/06/2026", "Concluída",  "R$ 530,00"},
        {"#0036", "Lucas Mello",    "Tracker 2023", "02/06/2026", "Andamento",  "R$ 1.200,00"},
    };

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
        JPanel barra = new JPanel(new BorderLayout(12, 0));
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(0, 0, 14, 0));

        // Busca
        txtBusca = new JTextField();
        txtBusca.setFont(MainFrame.FONT_NORMAL);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setToolTipText("Buscar OS...");

        // Combo ordenação
        String[] opcoes = {
            "Ordenar por...",
            "Cliente A → Z", "Cliente Z → A",
            "Status: Concluída → Aberta", "Status: Aberta → Concluída",
            "Nº OS crescente", "Nº OS decrescente",
            "Valor crescente", "Valor decrescente"
        };
        JComboBox<String> cmbOrdem = new JComboBox<>(opcoes);
        cmbOrdem.setFont(MainFrame.FONT_NORMAL);
        cmbOrdem.setBackground(Color.WHITE);
        cmbOrdem.setPreferredSize(new Dimension(200, 34));

        // Botão Nova OS
        JButton btnNova = criarBotaoNavy("Nova OS", 110, 34);
        btnNova.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);
        direita.add(cmbOrdem);
        direita.add(btnNova);

        barra.add(txtBusca, BorderLayout.CENTER);
        barra.add(direita,  BorderLayout.EAST);
        return barra;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : DADOS_MOCK) modelo.addRow(row);

        tabela = new JTable(modelo);
        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(40);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionBackground(new Color(0xe8e3d8));
        tabela.setSelectionForeground(MainFrame.COR_NAVY);
        tabela.setFillsViewportHeight(true);
        tabela.setDefaultEditor(Object.class, null);

        // Header
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(MainFrame.COR_CREAM_ALT);
        header.setForeground(new Color(0x444444));
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainFrame.COR_BORDER));
        header.setReorderingAllowed(false);

        // Larguras
        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);

        // Renderer de status
        tabela.getColumnModel().getColumn(4).setCellRenderer(new StatusPillRenderer());

        // Renderer de ação
        tabela.getColumnModel().getColumn(6).setCellRenderer(new AcaoRenderer());

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

    // ── Renderers ─────────────────────────────────────────────────────────────
    static class StatusPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel(v == null ? "" : v.toString(), SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    String s = getText();
                    Color bg;
                    switch (s) {
                        case "Concluída": bg = new Color(0xE6F1FB); break;
                        case "Andamento": bg = new Color(0xFAEEDA); break;
                        default:          bg = new Color(0xE1F5EE); break;
                    }
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bg);
                    g2.fillRoundRect(4, (getHeight() - 22) / 2, getWidth() - 8, 22, 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            String s = v == null ? "" : v.toString();
            Color fg;
            switch (s) {
                case "Concluída": fg = new Color(0x185FA5); break;
                case "Andamento": fg = new Color(0x854F0B); break;
                default:          fg = new Color(0x0F6E56); break;
            }
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
