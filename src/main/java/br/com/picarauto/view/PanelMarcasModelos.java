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

public class PanelMarcasModelos extends JPanel {

    private final MainFrame frame;

    private boolean abaMarcas = true;
    private JPanel painelMarcas;
    private JPanel painelModelos;
    private DefaultTableModel modeloTabelaMarcas;
    private DefaultTableModel modeloTabelaModelos;

    private static final Object[][] DADOS_MARCAS = {
        {"1", "Chevrolet"},
        {"2", "Volkswagen"},
        {"3", "Fiat"},
        {"4", "Ford"},
        {"5", "Toyota"},
        {"6", "Honda"},
        {"7", "Hyundai"},
        {"8", "Renault"},
    };

    private static final Object[][] DADOS_MODELOS = {
        {"1", "Onix",    "Chevrolet", "2018"},
        {"2", "Tracker", "Chevrolet", "2020"},
        {"3", "Gol",     "Volkswagen","2015"},
        {"4", "Polo",    "Volkswagen","2019"},
        {"5", "Argo",    "Fiat",      "2017"},
        {"6", "Pulse",   "Fiat",      "2021"},
    };

    public PanelMarcasModelos(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(),  BorderLayout.NORTH);
        add(criarCorpo(),   BorderLayout.CENTER);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Marcas e modelos");
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
        corpo.add(criarToggleAbas(), BorderLayout.NORTH);

        painelMarcas  = criarPainelMarcas();
        painelModelos = criarPainelModelos();
        painelModelos.setVisible(false);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(painelMarcas,  BorderLayout.CENTER);
        conteudo.add(painelModelos, BorderLayout.SOUTH);

        corpo.add(conteudo, BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarToggleAbas() {
        JPanel toggle = new JPanel(new BorderLayout());
        toggle.setBackground(MainFrame.COR_CREAM_ALT);
        toggle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xd0cbc0)));

        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);
        abas.setBorder(new EmptyBorder(8, 20, 0, 20));

        JButton btnMarcas  = criarAba("Marcas",  true);
        JButton btnModelos = criarAba("Modelos", false);

        btnMarcas.addActionListener(e -> {
            abaMarcas = true;
            painelMarcas.setVisible(true);
            painelModelos.setVisible(false);
            abas.repaint();
        });
        btnModelos.addActionListener(e -> {
            abaMarcas = false;
            painelMarcas.setVisible(false);
            painelModelos.setVisible(true);
            abas.repaint();
        });

        abas.add(btnMarcas);
        abas.add(btnModelos);
        toggle.add(abas, BorderLayout.WEST);
        return toggle;
    }

    private JButton criarAba(String label, boolean isMarcas) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                boolean ativo = (isMarcas == abaMarcas);
                if (ativo) {
                    g2.setColor(MainFrame.COR_CREAM);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(MainFrame.COR_NAVY);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
                }
                g2.setColor(ativo ? MainFrame.COR_NAVY : new Color(0x888888));
                g2.setFont(new Font("Segoe UI", ativo ? Font.BOLD : Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel criarPainelMarcas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);

        JPanel toolbar = criarToolbar("+ Nova marca", () -> abrirDialogNovaMarca());
        painel.add(toolbar, BorderLayout.NORTH);

        modeloTabelaMarcas = new DefaultTableModel(null, new String[]{"ID", "Nome", ""}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] linha : DADOS_MARCAS) {
            modeloTabelaMarcas.addRow(new Object[]{linha[0], linha[1], "Editar"});
        }

        painel.add(criarTabela(modeloTabelaMarcas, new int[]{50, -1, 70}), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelModelos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);

        JPanel toolbar = criarToolbar("+ Novo modelo", () -> {});
        painel.add(toolbar, BorderLayout.NORTH);

        modeloTabelaModelos = new DefaultTableModel(null, new String[]{"ID", "Modelo", "Marca", "Ano", ""}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] linha : DADOS_MODELOS) {
            modeloTabelaModelos.addRow(new Object[]{linha[0], linha[1], linha[2], linha[3], "Editar"});
        }

        painel.add(criarTabela(modeloTabelaModelos, new int[]{50, -1, 120, 80, 70}), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarToolbar(String labelBtn, Runnable acao) {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(MainFrame.COR_CREAM_ALT);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton btn = new JButton(labelBtn) {
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
        btn.setPreferredSize(new Dimension(130, 32));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> acao.run());

        toolbar.add(btn, BorderLayout.WEST);
        return toolbar;
    }

    private JScrollPane criarTabela(DefaultTableModel modelo, int[] larguras) {
        JTable tabela = new JTable(modelo) {
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
        tabela.setRowHeight(36);
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

        for (int i = 0; i < larguras.length; i++) {
            if (larguras[i] > 0) tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
            DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    lbl.setBorder(new EmptyBorder(0, 16, 0, 8));
                    lbl.setBackground(sel ? t.getSelectionBackground()
                        : row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                    lbl.setOpaque(true);
                    return lbl;
                }
            };
            tabela.getColumnModel().getColumn(i).setCellRenderer(r);
        }

        tabela.getColumnModel().getColumn(larguras.length - 1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
                p.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                JLabel btn = new JLabel("Editar") {
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
                btn.setPreferredSize(new Dimension(52, 24));
                btn.setOpaque(false);
                p.add(btn);
                return p;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xe0dbd0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private void abrirDialogNovaMarca() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Nova marca");
        dialog.setModal(true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel corpo = new JPanel();
        corpo.setBackground(MainFrame.COR_CREAM);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel("Nome da marca");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtNome = new JTextField();
        txtNome.setFont(MainFrame.FONT_NORMAL);
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        txtNome.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        corpo.add(lbl);
        corpo.add(Box.createVerticalStrut(6));
        corpo.add(txtNome);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        rodape.setBackground(MainFrame.COR_CREAM_ALT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(MainFrame.COR_NAVY);
        btnSalvar.setForeground(MainFrame.COR_GOLD);
        btnSalvar.setBorderPainted(false);
        btnSalvar.addActionListener(e -> {
            if (!txtNome.getText().isBlank()) {
                int novoId = modeloTabelaMarcas.getRowCount() + 1;
                modeloTabelaMarcas.addRow(new Object[]{novoId, txtNome.getText().trim(), "Editar"});
                dialog.dispose();
            }
        });

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);

        dialog.add(corpo,   BorderLayout.CENTER);
        dialog.add(rodape,  BorderLayout.SOUTH);
        dialog.setVisible(true);
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
