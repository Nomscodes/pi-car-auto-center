package br.com.picarauto.view;

/**
 * Composição / detalhe de uma Ordem de Serviço.
 * Card com cabeçalho, status pill, serviços, peças e total.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelComposicaoOS extends JPanel {

    private final MainFrame frame;

    public PanelComposicaoOS(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_COMPOSICAO), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Composição da OS");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        bar.add(lbl,       BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JScrollPane criarScrollConteudo() {
        JPanel p = new JPanel();
        p.setBackground(MainFrame.COR_CREAM);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Cabeçalho da OS
        p.add(criarCabecalho());
        p.add(Box.createVerticalStrut(16));

        // Info row: colaborador + telefone
        p.add(criarInfoRow());
        p.add(Box.createVerticalStrut(20));

        // Seção serviços
        p.add(criarLabelSecao("Serviços"));
        p.add(Box.createVerticalStrut(8));
        p.add(criarTabelaItens(new String[][]{
            {"Troca de Óleo",              "Interno", "1", "R$ 80,00"},
            {"Filtro de Óleo",             "Interno", "1", "R$ 45,00"},
            {"Alinhamento/Balanceamento",  "Externo", "1", "R$ 120,00"},
        }));
        p.add(Box.createVerticalStrut(20));

        // Seção peças
        p.add(criarLabelSecao("Peças Utilizadas"));
        p.add(Box.createVerticalStrut(8));
        p.add(criarTabelaItens(new String[][]{
            {"Óleo Motor 5W30 1L", "—", "4", "R$ 32,00"},
            {"Filtro de Óleo OC90", "—", "1", "R$ 28,50"},
        }));
        p.add(Box.createVerticalStrut(20));

        // Total
        p.add(criarRodapeTotal());

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    // Cabeçalho: número, cliente, veículo, data, status, botão editar status
    private JPanel criarCabecalho() {
        JPanel card = criarCardBase();
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel esq = new JPanel();
        esq.setOpaque(false);
        esq.setLayout(new BoxLayout(esq, BoxLayout.Y_AXIS));

        JLabel lblNum = new JLabel("OS #0042");
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNum.setForeground(MainFrame.COR_NAVY);

        JLabel lblCliente = new JLabel("Cliente: Marcos Silva");
        lblCliente.setFont(MainFrame.FONT_NORMAL);
        lblCliente.setForeground(new Color(0x444444));

        JLabel lblVeiculo = new JLabel("Veículo: Chevrolet Onix 2022  —  Placa: ABC-1234");
        lblVeiculo.setFont(MainFrame.FONT_NORMAL);
        lblVeiculo.setForeground(new Color(0x444444));

        JLabel lblData = new JLabel("Abertura: 08/06/2026");
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblData.setForeground(MainFrame.COR_MUTED);

        esq.add(lblNum);
        esq.add(Box.createVerticalStrut(6));
        esq.add(lblCliente);
        esq.add(Box.createVerticalStrut(2));
        esq.add(lblVeiculo);
        esq.add(Box.createVerticalStrut(4));
        esq.add(lblData);

        JPanel dir = new JPanel();
        dir.setOpaque(false);
        dir.setLayout(new BoxLayout(dir, BoxLayout.Y_AXIS));

        JLabel pill = PanelDashboard.criarStatusPill("Andamento");
        pill.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnStatus = criarBotaoNavy("Editar status", 130, 32);
        btnStatus.setAlignmentX(Component.RIGHT_ALIGNMENT);

        dir.add(pill);
        dir.add(Box.createVerticalStrut(10));
        dir.add(btnStatus);

        card.add(esq, BorderLayout.CENTER);
        card.add(dir, BorderLayout.EAST);
        return card;
    }

    // Info row: colaborador e telefone
    private JPanel criarInfoRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);

        row.add(criarInfoChip("Colaborador", "João Mecânico"));
        row.add(criarInfoChip("Telefone do cliente", "(47) 99123-4567"));
        return row;
    }

    private JPanel criarInfoChip(String label, String valor) {
        JPanel p = criarCardBase();
        p.setBorder(new EmptyBorder(12, 16, 12, 16));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(0x888888));

        JLabel val = new JLabel(valor);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(MainFrame.COR_NAVY);

        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    // Tabela de itens (serviços ou peças)
    private JPanel criarTabelaItens(String[][] dados) {
        JPanel card = criarCardBase();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header
        JPanel header = new JPanel(new GridLayout(1, 4));
        header.setOpaque(false);
        header.setBackground(MainFrame.COR_CREAM_ALT);
        header.setBorder(new EmptyBorder(8, 16, 8, 16));

        for (String col : new String[]{"Descrição", "Tipo", "Qtd", "Valor"}) {
            JLabel h = new JLabel(col);
            h.setFont(new Font("Segoe UI", Font.BOLD, 11));
            h.setForeground(new Color(0x444444));
            header.add(h);
        }

        card.add(header);

        for (String[] row : dados) {
            JPanel linha = new JPanel(new GridLayout(1, 4));
            linha.setOpaque(false);
            linha.setBorder(new EmptyBorder(10, 16, 10, 16));

            JLabel lNome = new JLabel(row[0]);
            lNome.setFont(MainFrame.FONT_NORMAL);
            lNome.setForeground(new Color(0x333333));

            JLabel lTipo = new JLabel(row[1]);
            lTipo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lTipo.setForeground(MainFrame.COR_MUTED);

            JLabel lQtd = new JLabel(row[2]);
            lQtd.setFont(MainFrame.FONT_NORMAL);
            lQtd.setForeground(new Color(0x444444));

            JLabel lVal = new JLabel(row[3]);
            lVal.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lVal.setForeground(MainFrame.COR_NAVY);

            linha.add(lNome); linha.add(lTipo); linha.add(lQtd); linha.add(lVal);
            card.add(criarDivisorLinha());
            card.add(linha);
        }
        return card;
    }

    // Rodapé com total e botões
    private JPanel criarRodapeTotal() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);

        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        esq.setOpaque(false);
        esq.add(criarBotaoOutline("Imprimir OS", 130, 36));
        esq.add(criarBotaoNavy("Adicionar item", 140, 36));

        JPanel dir = new JPanel();
        dir.setOpaque(false);
        dir.setLayout(new BoxLayout(dir, BoxLayout.Y_AXIS));

        JLabel lblLabel = new JLabel("Total da OS");
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLabel.setForeground(new Color(0x666666));
        lblLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblTotal = new JLabel("R$ 385,50");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(MainFrame.COR_NAVY);
        lblTotal.setAlignmentX(Component.RIGHT_ALIGNMENT);

        dir.add(lblLabel);
        dir.add(lblTotal);

        p.add(esq, BorderLayout.WEST);
        p.add(dir, BorderLayout.EAST);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel criarLabelSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainFrame.COR_NAVY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarCardBase() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return card;
    }

    private JPanel criarDivisorLinha() {
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(MainFrame.COR_BORDER);
                g.fillRect(16, 0, getWidth() - 32, 1);
            }
        };
        d.setOpaque(false);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setPreferredSize(new Dimension(0, 1));
        return d;
    }

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

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(MainFrame.COR_NAVY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
