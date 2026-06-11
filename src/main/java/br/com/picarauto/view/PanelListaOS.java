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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PanelListaOS extends JPanel {

    private final MainFrame frame;
    private JTextField   txtBusca;
    private JTable       tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

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
            "Status: Concluída → Aberta", "Status: Aberta → Concluída",
            "Nº OS crescente", "Nº OS decrescente",
            "Valor crescente", "Valor decrescente"
        };
        JComboBox<String> cmbOrdem = new JComboBox<>(opcoes);
        cmbOrdem.setFont(MainFrame.FONT_NORMAL);
        cmbOrdem.setBackground(Color.WHITE);
        cmbOrdem.setPreferredSize(new Dimension(200, 34));

        JButton btnNova = criarBotaoNavy("Nova OS", 110, 34);
        btnNova.addActionListener(e -> abrirDialogNovaOS());

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

    // ── Dialog Nova OS ────────────────────────────────────────────────────────
    private void abrirDialogNovaOS() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Nova Ordem de Serviço", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dlg.setSize(500, 340);
        dlg.setLocationRelativeTo(this);

        JTextField txtCliente  = criarCampoDlg();
        JTextField txtColab    = criarCampoDlg();
        JTextField txtMarca    = criarCampoDlg();
        JTextField txtModelo   = criarCampoDlg();
        JTextField txtPlaca    = criarCampoDlg();

        JPanel grid = new JPanel(new java.awt.GridLayout(3, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        grid.add(criarGrupoDlg("Cliente",              txtCliente));
        grid.add(criarGrupoDlg("Colaborador",          txtColab));
        grid.add(criarGrupoDlg("Marca",                txtMarca));
        grid.add(criarGrupoDlg("Modelo",               txtModelo));
        grid.add(criarGrupoDlg("Placa",                txtPlaca));
        grid.add(new JPanel() {{ setOpaque(false); }});

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCanc = new JButton("Cancelar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new java.awt.geom.RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm2.stringWidth(getText()))/2, (getHeight()+fm2.getAscent()-fm2.getDescent())/2);
                g2.dispose();
            }
        };
        btnCanc.setOpaque(false); btnCanc.setContentAreaFilled(false); btnCanc.setBorderPainted(false);
        btnCanc.setFocusPainted(false); btnCanc.setForeground(MainFrame.COR_NAVY);
        btnCanc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCanc.setPreferredSize(new Dimension(100, 34));
        btnCanc.addActionListener(e -> dlg.dispose());

        JButton btnSalv = new JButton("Salvar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm2 = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm2.stringWidth(getText()))/2, (getHeight()+fm2.getAscent()-fm2.getDescent())/2);
                g2.dispose();
            }
        };
        btnSalv.setOpaque(true); btnSalv.setContentAreaFilled(false); btnSalv.setBorderPainted(false);
        btnSalv.setFocusPainted(false);
        btnSalv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalv.setPreferredSize(new Dimension(100, 34));
        btnSalv.addActionListener(e -> {
            String cliente = txtCliente.getText().trim();
            String veiculo = (txtMarca.getText().trim() + " " + txtModelo.getText().trim()).trim();
            if (veiculo.isEmpty()) veiculo = "-";
            int num = modelo.getRowCount() + 1;
            String numOS = "#" + String.format("%04d", num);
            String data = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            modelo.insertRow(0, new Object[]{numOS, cliente, veiculo, data, "Aberta", "R$ 0,00", ""});
            dlg.dispose();
        });

        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(grid);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dlg.add(form);
        dlg.setVisible(true);
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
