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

public class PanelListaClientes extends JPanel {

    private final MainFrame frame;
    private DefaultTableModel modeloTabela;

    private static final String[] COLUNAS = {"#", "Nome", "Tipo", "Telefone", "E-mail", "", ""};

    private static final Object[][] DADOS = {
        {"1", "João Silva",      "PF", "(62) 99999-1111", "joao@email.com"},
        {"2", "Maria Costa",     "PF", "(62) 99999-2222", "maria@email.com"},
        {"3", "Auto Peças Ltda", "PJ", "(62) 3333-4444",  "contato@autopecas.com"},
        {"4", "Carlos Melo",     "PF", "(62) 99999-3333", "carlos@email.com"},
        {"5", "Distribuidora XY","PJ", "(62) 3333-5555",  "xy@distribuidora.com"},
    };

    public PanelListaClientes(MainFrame frame) {
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

        JLabel lblTitulo = new JLabel("Clientes");
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

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(MainFrame.COR_CREAM_ALT);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton btnNovo = new JButton("+ Novo cliente") {
            @Override protected void paintComponent(Graphics g) {
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
        btnNovo.setPreferredSize(new Dimension(140, 32));
        btnNovo.setBorderPainted(false);
        btnNovo.setContentAreaFilled(false);
        btnNovo.setFocusPainted(false);
        btnNovo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNovo.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_CLIENTE));

        toolbar.add(btnNovo, BorderLayout.WEST);
        corpo.add(toolbar, BorderLayout.NORTH);
        corpo.add(criarTabela(), BorderLayout.CENTER);
        return corpo;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(null, COLUNAS) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] linha : DADOS) {
            modeloTabela.addRow(new Object[]{linha[0], linha[1], linha[2], linha[3], linha[4], "Editar", "Excluir"});
        }

        JTable tabela = new JTable(modeloTabela) {
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

        int[] larguras = {40, -1, 50, 150, 200, 70, 70};
        for (int i = 0; i < larguras.length - 2; i++) {
            if (larguras[i] > 0) tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
            DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    lbl.setBorder(new EmptyBorder(0, 16, 0, 8));
                    lbl.setBackground(sel ? t.getSelectionBackground() : row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                    lbl.setOpaque(true);
                    return lbl;
                }
            };
            tabela.getColumnModel().getColumn(i).setCellRenderer(r);
        }

        // Badge tipo PF/PJ
        tabela.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
                p.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                boolean isPF = "PF".equals(val);
                JLabel badge = new JLabel(val == null ? "" : val.toString()) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(isPF ? new Color(0xcfe2ff) : new Color(0xfff3cd));
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                badge.setForeground(isPF ? new Color(0x084298) : new Color(0x856404));
                badge.setOpaque(false);
                badge.setBorder(new EmptyBorder(3, 8, 3, 8));
                p.add(badge);
                return p;
            }
        });

        // Botão Editar
        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(5).setCellRenderer(criarBotaoRenderer("Editar", MainFrame.COR_NAVY, MainFrame.COR_GOLD));

        // Botão Excluir
        tabela.getColumnModel().getColumn(6).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(6).setCellRenderer(criarBotaoRenderer("Excluir", new Color(0x8B1A1A), Color.WHITE));

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                int row = tabela.rowAtPoint(e.getPoint());
                int col = tabela.columnAtPoint(e.getPoint());
                if (row < 0) return;
                if (col == 5) frame.mostrarTela(MainFrame.TELA_CLIENTE);
                if (col == 6) confirmarExclusao(row);
            }
        });
        tabela.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                tabela.setCursor(col >= 5 ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xe0dbd0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private TableCellRenderer criarBotaoRenderer(String label, Color bg, Color fg) {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
                p.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xfaf8f4));
                JLabel btn = new JLabel(label) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(bg);
                        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                        g2.setColor(fg);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                        g2.dispose();
                    }
                };
                btn.setPreferredSize(new Dimension(56, 26));
                btn.setOpaque(false);
                p.add(btn);
                return p;
            }
        };
    }

    private void confirmarExclusao(int row) {
        String nome = (String) modeloTabela.getValueAt(row, 1);
        int opcao = JOptionPane.showConfirmDialog(this,
            "<html><b>Excluir cliente: " + nome + "?</b><br><br>" +
            "Serão removidos também:<br>" +
            "• Dados pessoais (CPF/CNPJ)<br>" +
            "• Veículos vinculados<br><br>" +
            "<font color='gray'>As Ordens de Serviço e histórico de rastreabilidade<br>serão mantidos para fins de auditoria.</font></html>",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (opcao == JOptionPane.YES_OPTION) {
            modeloTabela.removeRow(row);
            JOptionPane.showMessageDialog(this,
                "Cliente excluído com sucesso.\nDados removidos: cliente, pessoa e veículos.",
                "Exclusão concluída", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setStroke(new java.awt.BasicStroke(0.8f));
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
