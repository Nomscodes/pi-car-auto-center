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

    private DefaultTableModel modeloTabela;
    private JTable tabela;
    private JTextField txtBusca;
    private JPopupMenu menuOrdenar;
    private String filtroAtivo = "TODAS";
    private String ordemAtiva = "num-desc";

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

    private static final java.util.Map<String, Color[]> BADGE_CORES = new java.util.HashMap<>();
    private static final java.util.Map<String, String>  BADGE_LABEL = new java.util.HashMap<>();
    static {
        BADGE_CORES.put("ORCAMENTO",  new Color[]{new Color(0xfff3cd), new Color(0x856404)});
        BADGE_CORES.put("EXECUCAO",   new Color[]{new Color(0xcfe2ff), new Color(0x084298)});
        BADGE_CORES.put("PAGAMENTO",  new Color[]{new Color(0xfff3cd), new Color(0x664d03)});
        BADGE_CORES.put("FINALIZADO", new Color[]{new Color(0xd1e7dd), new Color(0x0a3622)});
        BADGE_LABEL.put("ORCAMENTO",  "Orçamento");
        BADGE_LABEL.put("EXECUCAO",   "Execução");
        BADGE_LABEL.put("PAGAMENTO",  "Ag. Pagamento");
        BADGE_LABEL.put("FINALIZADO", "Finalizada");
    }

    // Cards de resumo
    private JPanel[] cardsResumo = new JPanel[4];
    private static final String[] STATUS_CARDS = {"ORCAMENTO", "EXECUCAO", "PAGAMENTO", "FINALIZADO"};
    private static final Color[]  DOTS = {new Color(0xf0c040), new Color(0x4a90d9), new Color(0xf0a030), new Color(0x1a2744)};

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
        corpo.add(criarResumo(),  BorderLayout.NORTH);
        corpo.add(criarTabela(),  BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarResumo() {
        JPanel resumo = new JPanel(new GridLayout(1, 4, 12, 0));
        resumo.setBackground(MainFrame.COR_CREAM);
        resumo.setBorder(new EmptyBorder(14, 20, 14, 20));

        long[] contagens = new long[4];
        for (Object[] linha : DADOS) {
            for (int i = 0; i < STATUS_CARDS.length; i++) {
                if (STATUS_CARDS[i].equals(linha[5])) contagens[i]++;
            }
        }

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            final String status = STATUS_CARDS[i];
            final Color dot = DOTS[i];

            JPanel card = new JPanel(new BorderLayout()) {
                private boolean hover = false;
                {
                    setBackground(Color.WHITE);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xe0dbd0), 1),
                        new EmptyBorder(12, 14, 12, 14)));
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    addMouseListener(new MouseAdapter() {
                        @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                        @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                        @Override public void mouseClicked(MouseEvent e) {
                            filtroAtivo = status.equals(filtroAtivo) ? "TODAS" : status;
                            renderizarTabela();
                            for (JPanel c : cardsResumo) c.repaint();
                        }
                    });
                }
                @Override protected void paintBorder(Graphics g) {
                    boolean ativo = filtroAtivo.equals(status);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(ativo ? MainFrame.COR_GOLD : (hover ? MainFrame.COR_NAVY : new Color(0xe0dbd0)));
                    g2.setStroke(new BasicStroke(ativo ? 2f : 1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                    g2.dispose();
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    boolean ativo = filtroAtivo.equals(status);
                    g2.setColor(ativo ? new Color(0xfffbf4) : Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);

            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            labelPanel.setOpaque(false);
            JPanel dotPanel = new JPanel() {
                { setOpaque(false); setPreferredSize(new Dimension(10, 10)); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(dot);
                    g2.fillOval(0, 1, 8, 8);
                    g2.dispose();
                }
            };
            JLabel lblStatus = new JLabel(" " + BADGE_LABEL.getOrDefault(status, status));
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblStatus.setForeground(new Color(0x888888));
            labelPanel.add(dotPanel);
            labelPanel.add(lblStatus);

            JLabel lblNum = new JLabel(String.valueOf(contagens[idx]));
            lblNum.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblNum.setForeground(MainFrame.COR_NAVY);

            card.add(labelPanel, BorderLayout.NORTH);
            card.add(lblNum,     BorderLayout.CENTER);
            cardsResumo[i] = card;
            resumo.add(card);
        }
        return resumo;
    }

    private JPanel criarTabela() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(MainFrame.COR_CREAM_ALT);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esq.setOpaque(false);

        JButton btnNova = criarBotaoPrimario("+ Nova OS");
        btnNova.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));
        esq.add(btnNova);

        txtBusca = new JTextField(20);
        txtBusca.setFont(MainFrame.FONT_NORMAL);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(5, 10, 5, 10)));
        txtBusca.setToolTipText("Buscar por nome ou nº OS");
        txtBusca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { renderizarTabela(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { renderizarTabela(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { renderizarTabela(); }
        });
        esq.add(new JLabel("Buscar:"));
        esq.add(txtBusca);

        criarMenuOrdenar();
        JButton btnOrdenar = new JButton("Ordenar ▼");
        btnOrdenar.setFont(MainFrame.FONT_SMALL);
        btnOrdenar.setBackground(Color.WHITE);
        btnOrdenar.setForeground(new Color(0x555555));
        btnOrdenar.setBorder(BorderFactory.createLineBorder(new Color(0xbbbbbb), 1));
        btnOrdenar.setFocusPainted(false);
        btnOrdenar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOrdenar.addActionListener(e -> menuOrdenar.show(btnOrdenar, 0, btnOrdenar.getHeight()));

        toolbar.add(esq, BorderLayout.WEST);
        toolbar.add(btnOrdenar, BorderLayout.EAST);
        wrapper.add(toolbar, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel(null, COLUNAS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                return c;
            }
        };
        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(38);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(new Color(0xf0ebe0));
        tabela.setFocusable(false);

        JTableHeader cab = tabela.getTableHeader();
        cab.setBackground(MainFrame.COR_NAVY);
        cab.setForeground(MainFrame.COR_GOLD);
        cab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cab.setPreferredSize(new Dimension(0, 34));
        cab.setReorderingAllowed(false);

        int[] larguras = {70, -1, 130, 90, 100, 120, 70};
        for (int i = 0; i < larguras.length - 2; i++) {
            if (larguras[i] > 0) tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
            final int col = i;
            tabela.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int c) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, c);
                    lbl.setBorder(new EmptyBorder(0, 16, 0, 8));
                    lbl.setBackground(sel ? t.getSelectionBackground() : row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                    lbl.setOpaque(true);
                    if (col == 0) { lbl.setFont(new Font("Segoe UI", Font.BOLD, 12)); lbl.setForeground(MainFrame.COR_NAVY); }
                    else { lbl.setFont(MainFrame.FONT_NORMAL); lbl.setForeground(new Color(0x333333)); }
                    return lbl;
                }
            });
        }

        tabela.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
                p.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                String st = val == null ? "" : val.toString();
                Color[] cores = BADGE_CORES.getOrDefault(st, new Color[]{new Color(0xeeeeee), Color.DARK_GRAY});
                String label  = BADGE_LABEL.getOrDefault(st, st);
                JLabel badge = new JLabel(label) {
                    @Override protected void paintComponent(Graphics g) {
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
                p.add(badge);
                return p;
            }
        });

        tabela.getColumnModel().getColumn(6).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
                p.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                JLabel btn = new JLabel("Abrir") {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(MainFrame.COR_NAVY);
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                        g2.setColor(MainFrame.COR_GOLD);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                            (getHeight()+fm.getAscent()-fm.getDescent())/2);
                        g2.dispose();
                    }
                };
                btn.setPreferredSize(new Dimension(52, 26));
                btn.setOpaque(false);
                p.add(btn);
                return p;
            }
        });

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                int row = tabela.rowAtPoint(e.getPoint());
                int col = tabela.columnAtPoint(e.getPoint());
                if (col == 6 && row >= 0) frame.mostrarTela(MainFrame.TELA_COMPOSICAO);
            }
        });
        tabela.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                tabela.setCursor(tabela.columnAtPoint(e.getPoint()) == 6
                    ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xe0dbd0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        wrapper.add(scroll, BorderLayout.CENTER);

        renderizarTabela();
        return wrapper;
    }

    private void criarMenuOrdenar() {
        menuOrdenar = new JPopupMenu();
        menuOrdenar.setBorder(BorderFactory.createLineBorder(new Color(0xd0cbc0), 1));

        adicionarItemMenu("Nº OS crescente",    "num-asc");
        adicionarItemMenu("Nº OS decrescente",  "num-desc");
        menuOrdenar.addSeparator();
        adicionarItemMenu("Nome A → Z",         "nome-asc");
        adicionarItemMenu("Nome Z → A",         "nome-desc");
        menuOrdenar.addSeparator();
        adicionarItemMenu("Agrupar por status", "status");
    }

    private void adicionarItemMenu(String label, String ordem) {
        JMenuItem item = new JMenuItem(label);
        item.setFont(MainFrame.FONT_NORMAL);
        item.addActionListener(e -> { ordemAtiva = ordem; renderizarTabela(); });
        menuOrdenar.add(item);
    }

    private void renderizarTabela() {
        String busca = txtBusca != null ? txtBusca.getText().trim().toLowerCase() : "";

        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        for (Object[] linha : DADOS) {
            if (!filtroAtivo.equals("TODAS") && !filtroAtivo.equals(linha[5])) continue;
            if (!busca.isEmpty()) {
                String num = ((String) linha[0]).toLowerCase();
                String cli = ((String) linha[1]).toLowerCase();
                if (!num.contains(busca) && !cli.contains(busca)) continue;
            }
            lista.add(linha);
        }

        lista.sort((a, b) -> {
            switch (ordemAtiva) {
                case "num-asc":  return ((String)a[0]).compareTo((String)b[0]);
                case "nome-asc": return ((String)a[1]).compareTo((String)b[1]);
                case "nome-desc":return ((String)b[1]).compareTo((String)a[1]);
                case "status":   return ((String)a[5]).compareTo((String)b[5]);
                default:         return ((String)b[0]).compareTo((String)a[0]);
            }
        });

        modeloTabela.setRowCount(0);
        for (Object[] linha : lista) {
            modeloTabela.addRow(new Object[]{linha[0], linha[1], linha[2], linha[3], linha[4], linha[5], "Abrir"});
        }
    }

    private JButton criarBotaoPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 6, 6));
                g2.setFont(MainFrame.FONT_SMALL);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
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
