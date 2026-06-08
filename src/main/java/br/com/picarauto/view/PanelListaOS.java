package br.com.picarauto.view;

/**
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class PanelListaOS extends JPanel {

    private final MainFrame frame;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private String filtroAtivo = "Todas";

    private static final String[] COLUNAS = {"Nº OS", "Cliente", "Veículo", "Data", "Valor", "Status", ""};

    private static final Object[][] DADOS = {
        {"#0042", "João Silva",   "Onix 2022",    "07/06/2026", "R$ 850,00",   "EXECUCAO"},
        {"#0041", "Maria Costa",  "HB20 2021",    "06/06/2026", "R$ 1.200,00", "PAGAMENTO"},
        {"#0040", "Carlos Melo",  "Gol 2019",     "05/06/2026", "R$ 320,00",   "ORCAMENTO"},
        {"#0039", "Ana Souza",    "Kwid 2023",    "04/06/2026", "R$ 540,00",   "FINALIZADO"},
        {"#0038", "Pedro Lima",   "Corolla 2020", "03/06/2026", "R$ 2.100,00", "EXECUCAO"},
        {"#0037", "Lucas Alves",  "Creta 2022",   "02/06/2026", "R$ 780,00",   "ORCAMENTO"},
        {"#0036", "Fernanda Rua", "Pulse 2023",   "01/06/2026", "R$ 450,00",   "FINALIZADO"},
    };

    private static final String[] FILTROS = {"Todas", "Orçamento", "Execução", "Ag. Pagamento", "Finalizada"};

    public PanelListaOS(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(),   BorderLayout.NORTH);
        add(criarCorpo(),    BorderLayout.CENTER);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Ordens de serviço");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setOpaque(false);
        corpo.add(criarToolbar(), BorderLayout.NORTH);
        corpo.add(criarTabela(), BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(MainFrame.COR_CREAM_ALT);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esquerda.setOpaque(false);

        JButton btnNova = new JButton("+ Nova OS") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnNova.setPreferredSize(new Dimension(100, 32));
        btnNova.setBorderPainted(false);
        btnNova.setContentAreaFilled(false);
        btnNova.setFocusPainted(false);
        btnNova.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNova.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        esquerda.add(btnNova);

        for (String filtro : FILTROS) {
            esquerda.add(criarBotaoFiltro(filtro));
        }

        JButton btnOrdenar = new JButton("Ordenar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xf0ebe0) : Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(new Color(0xbbbbbb));
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
                g2.setColor(new Color(0x555555));
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnOrdenar.setPreferredSize(new Dimension(90, 28));
        btnOrdenar.setBorderPainted(false);
        btnOrdenar.setContentAreaFilled(false);
        btnOrdenar.setFocusPainted(false);
        btnOrdenar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        toolbar.add(esquerda,   BorderLayout.WEST);
        toolbar.add(btnOrdenar, BorderLayout.EAST);
        return toolbar;
    }

    private JButton criarBotaoFiltro(String label) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = label.equals(filtroAtivo);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(ativo ? MainFrame.COR_GOLD : new Color(0x555555));
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(label.length() * 7 + 20, 28));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            filtroAtivo = label;
            repaint();
        });
        return btn;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(null, COLUNAS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Object[] linha : DADOS) {
            modeloTabela.addRow(new Object[]{
                linha[0], linha[1], linha[2], linha[3], linha[4], linha[5], "Abrir"
            });
        }

        tabela = new JTable(modeloTabela) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                }
                return c;
            }
        };

        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(38);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(new Color(0xf0ebe0));
        tabela.setSelectionForeground(MainFrame.COR_NAVY);
        tabela.setFocusable(false);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setBackground(MainFrame.COR_NAVY);
        cabecalho.setForeground(MainFrame.COR_GOLD);
        cabecalho.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cabecalho.setPreferredSize(new Dimension(0, 36));
        cabecalho.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) cabecalho.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(70);

        tabela.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        tabela.getColumnModel().getColumn(6).setCellRenderer(new BotaoAbrirRenderer());

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                if (col == 6) frame.mostrarTela(MainFrame.TELA_COMPOSICAO);
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBorder(new EmptyBorder(0, 16, 0, 8));
        for (int i = 0; i < 5; i++) tabela.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xe0dbd0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private static class StatusRenderer extends DefaultTableCellRenderer {
        private static final java.util.Map<String, Color[]> CORES = new java.util.HashMap<>();
        static {
            CORES.put("ORCAMENTO",  new Color[]{new Color(0xfff3cd), new Color(0x856404)});
            CORES.put("EXECUCAO",   new Color[]{new Color(0xcfe2ff), new Color(0x084298)});
            CORES.put("PAGAMENTO",  new Color[]{new Color(0xfff3cd), new Color(0x664d03)});
            CORES.put("FINALIZADO", new Color[]{new Color(0xd1e7dd), new Color(0x0a3622)});
        }
        private static final java.util.Map<String, String> LABELS = new java.util.HashMap<>();
        static {
            LABELS.put("ORCAMENTO",  "Orçamento");
            LABELS.put("EXECUCAO",   "Execução");
            LABELS.put("PAGAMENTO",  "Ag. Pagamento");
            LABELS.put("FINALIZADO", "Finalizada");
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
            painel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));

            String status = val == null ? "" : val.toString();
            Color[] cores = CORES.getOrDefault(status, new Color[]{new Color(0xeeeeee), Color.DARK_GRAY});
            String label  = LABELS.getOrDefault(status, status);

            JLabel badge = new JLabel(label) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(cores[0]);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
            badge.setForeground(cores[1]);
            badge.setOpaque(false);
            badge.setBorder(new EmptyBorder(3, 10, 3, 10));

            painel.add(badge);
            return painel;
        }
    }

    private static class BotaoAbrirRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
            painel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));

            JLabel btn = new JLabel("Abrir") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(MainFrame.COR_NAVY);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                    g2.setColor(MainFrame.COR_GOLD);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            };
            btn.setPreferredSize(new Dimension(52, 26));
            btn.setOpaque(false);
            painel.add(btn);
            return painel;
        }
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
                g2.setFont(MainFrame.FONT_SMALL);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
