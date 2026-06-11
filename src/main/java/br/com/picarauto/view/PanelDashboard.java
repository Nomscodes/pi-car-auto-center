package br.com.picarauto.view;

/**
 * Dashboard principal — 3 cards de métricas + OS recentes + serviços mais executados.
 * Layout: topbar (NORTH) + conteúdo cream (CENTER) + sidebar navy (EAST).
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelDashboard extends JPanel {

    private final MainFrame frame;

    public PanelDashboard(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_DASHBOARD), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblTitulo = new JLabel("AV CAR AUTO CENTER  —  Dashboard");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);

        bar.add(lblTitulo,         BorderLayout.WEST);
        bar.add(criarUsuarioPanel(), BorderLayout.EAST);
        return bar;
    }

    private JPanel criarUsuarioPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 9));
        p.setOpaque(false);

        JPanel avatar = new JPanel() {
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
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(30, 30));

        JLabel lblNome = new JLabel(MainFrame.getUsuarioLogado());
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNome.setForeground(new Color(0xccddff));

        p.add(avatar);
        p.add(lblNome);
        return p;
    }

    // ── Conteudo ──────────────────────────────────────────────────────────────
    private JScrollPane criarScrollConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(MainFrame.COR_CREAM);
        conteudo.setBorder(new EmptyBorder(24, 24, 24, 24));

        conteudo.add(criarCardsMetricas(), BorderLayout.NORTH);
        conteudo.add(criarCardsInferiores(), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarCardsMetricas() {
        JPanel p = new JPanel(new GridLayout(1, 3, 16, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 20, 0));

        p.add(criarCardMetrica("OS do mês",    "127",       "ordens abertas"));
        p.add(criarCardMetrica("Faturamento",  "R$ 48.920", "mês atual"));
        p.add(criarCardMetrica("Em andamento", "14",         "em execução agora"));
        return p;
    }

    private JPanel criarCardMetrica(String label, String valor, String sub) {
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
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setPreferredSize(new Dimension(0, 90));

        JLabel l1 = new JLabel(label);
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l1.setForeground(new Color(0x666666));

        JLabel l2 = new JLabel(valor);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l2.setForeground(MainFrame.COR_NAVY);

        JLabel l3 = new JLabel(sub);
        l3.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l3.setForeground(MainFrame.COR_MUTED);

        card.add(l1);
        card.add(Box.createVerticalStrut(6));
        card.add(l2);
        card.add(Box.createVerticalStrut(2));
        card.add(l3);
        return card;
    }

    private JPanel criarCardsInferiores() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setOpaque(false);

        p.add(criarCardOSRecentes());
        p.add(criarCardServicos());
        return p;
    }

    // OS Recentes
    private JPanel criarCardOSRecentes() {
        JPanel card = criarCardBase("OS Recentes");

        String[][] dados = {
            {"#0042", "Marcos Silva",  "Onix 2022",  "Concluída"},
            {"#0041", "Ana Pereira",   "HB20 2021",  "Andamento"},
            {"#0040", "Roberto Leal",  "Gol 2019",   "Aberta"},
            {"#0039", "Carla Moura",   "Pulse 2023", "Concluída"},
            {"#0038", "Fábio Nunes",  "Kwid 2020",  "Aberta"},
        };

        boolean primeiro = true;
        for (String[] r : dados) {
            if (!primeiro) card.add(criarSeparador());
            card.add(criarLinhaOS(r[0], r[1], r[2], r[3]));
            primeiro = false;
        }
        return card;
    }

    private JPanel criarLinhaOS(String num, String cliente, String veiculo, String status) {
        JPanel linha = new JPanel(new BorderLayout(8, 0));
        linha.setOpaque(false);
        linha.setBorder(new EmptyBorder(8, 0, 8, 0));

        JPanel esq = new JPanel(new BorderLayout(0, 2));
        esq.setOpaque(false);

        JLabel lblNum = new JLabel(num + "  " + cliente);
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNum.setForeground(MainFrame.COR_NAVY);

        JLabel lblVei = new JLabel(veiculo);
        lblVei.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVei.setForeground(new Color(0x888888));

        esq.add(lblNum, BorderLayout.NORTH);
        esq.add(lblVei, BorderLayout.SOUTH);

        linha.add(esq, BorderLayout.CENTER);
        linha.add(criarStatusPill(status), BorderLayout.EAST);
        return linha;
    }

    static JLabel criarStatusPill(String status) {
        Color bg, fg;
        switch (status) {
            case "Concluída": bg = new Color(0xE6F1FB); fg = new Color(0x185FA5); break;
            case "Andamento":  bg = new Color(0xFAEEDA); fg = new Color(0x854F0B); break;
            default:           bg = new Color(0xE1F5EE); fg = new Color(0x0F6E56); break;
        }
        JLabel lbl = new JLabel(status, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(fg);
        lbl.setOpaque(false);
        lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
        lbl.setPreferredSize(new Dimension(78, 22));
        return lbl;
    }

    // Serviços mais executados
    private JPanel criarCardServicos() {
        JPanel card = criarCardBase("Serviços Mais Executados");

        String[][] servicos = {
            {"Troca de Óleo",               "87"},
            {"Alinhamento/Balanceamento",   "62"},
            {"Revisão Geral",               "41"},
            {"Freios",                     "38"},
            {"Filtro de Ar",               "29"},
        };

        for (String[] s : servicos) {
            card.add(criarLinhaServico(s[0], Integer.parseInt(s[1]), 87));
            card.add(Box.createVerticalStrut(4));
        }
        return card;
    }

    private JPanel criarLinhaServico(String nome, int qtd, int max) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNome.setForeground(new Color(0x444444));
        lblNome.setPreferredSize(new Dimension(165, 14));

        JPanel barra = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_CREAM_ALT);
                g2.fillRoundRect(0, 4, getWidth(), 8, 4, 4);
                int w = (int) (getWidth() * qtd / (double) max);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fillRoundRect(0, 4, w, 8, 4, 4);
                g2.dispose();
            }
        };
        barra.setOpaque(false);
        barra.setPreferredSize(new Dimension(0, 16));

        JLabel lblQtd = new JLabel(String.valueOf(qtd));
        lblQtd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblQtd.setForeground(MainFrame.COR_MUTED);
        lblQtd.setPreferredSize(new Dimension(28, 14));

        p.add(lblNome, BorderLayout.WEST);
        p.add(barra,   BorderLayout.CENTER);
        p.add(lblQtd,  BorderLayout.EAST);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JPanel criarCardBase(String titulo) {
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
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(MainFrame.COR_NAVY);
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(12));
        return card;
    }

    private JPanel criarSeparador() {
        JPanel sep = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(MainFrame.COR_BORDER);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(0, 1));
        return sep;
    }
}
