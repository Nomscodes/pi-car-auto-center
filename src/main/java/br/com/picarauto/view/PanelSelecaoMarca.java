package br.com.picarauto.view;

/**
 * Seleção de marca do veículo — grid 4 colunas com logos reais.
 * Clique simples seleciona; duplo clique avança para TELA_MODELO.
 * Fallback: círculo colorido com sigla se imagem não encontrada.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PanelSelecaoMarca extends JPanel {

    private final MainFrame frame;
    public static boolean modoNovaOS = false;

    private String  marcaSelecionada = null;
    private JButton btnProximo;
    private JLabel  lblMarcaSel;

    // {sigla, corFundo, corTexto, nome, fileKey}
    public static final Object[][] MARCAS = {
        {"GM", new Color(0xFFD700), new Color(0x1a1a1a), "Chevrolet",    "chevrolet"},
        {"VW", new Color(0x001e50), Color.WHITE,          "Volkswagen",   "volkswagen"},
        {"FI", new Color(0xc8102e), Color.WHITE,          "Fiat",         "fiat"},
        {"FO", new Color(0x003da5), Color.WHITE,          "Ford",         "ford"},
        {"TO", new Color(0xeb0a1e), Color.WHITE,          "Toyota",       "toyota"},
        {"HO", new Color(0xe40521), Color.WHITE,          "Honda",        "honda"},
        {"HY", new Color(0x002c5f), Color.WHITE,          "Hyundai",      "hyundai"},
        {"RN", new Color(0xefdf00), new Color(0x333333),  "Renault",      "renault"},
        {"NI", new Color(0xc3002f), Color.WHITE,          "Nissan",       "nissan"},
        {"JP", new Color(0x006a4e), Color.WHITE,          "Jeep",         "jeep"},
        {"PG", new Color(0x1f3c88), Color.WHITE,          "Peugeot",      "pegeout"},
        {"CT", new Color(0xc41a1a), Color.WHITE,          "Citroën",      "citroen"},
        {"MI", new Color(0xce1126), Color.WHITE,          "Mitsubishi",   "mitsubishi"},
        {"KI", new Color(0x05141f), Color.WHITE,          "Kia",          "kia"},
        {"SU", new Color(0x003399), Color.WHITE,          "Subaru",       "subaru"},
        {"MB", new Color(0x1c1c1c), new Color(0xc0c0c0),  "Mercedes-Benz","mercedesbenz"},
    };

    public PanelSelecaoMarca(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollGrade(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_MARCA), BorderLayout.EAST);
        inner.add(criarRodape(), BorderLayout.SOUTH);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Selecionar Marca");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(
            modoNovaOS ? MainFrame.TELA_LISTA_OS : MainFrame.TELA_DASHBOARD));

        bar.add(lbl,       BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Grade ─────────────────────────────────────────────────────────────────
    private JScrollPane criarScrollGrade() {
        JPanel instrucao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instrucao.setOpaque(false);
        instrucao.setBorder(new EmptyBorder(16, 20, 0, 20));
        JLabel lbl = new JLabel("Clique para selecionar · duplo clique para avançar:");
        lbl.setFont(MainFrame.FONT_NORMAL);
        lbl.setForeground(new Color(0x555555));
        instrucao.add(lbl);

        JPanel grade = new JPanel(new GridLayout(4, 4, 16, 16));
        grade.setOpaque(false);
        grade.setBorder(new EmptyBorder(12, 20, 12, 20));

        for (Object[] marca : MARCAS) {
            grade.add(criarCardMarca(
                (String) marca[0],
                (Color)  marca[1],
                (Color)  marca[2],
                (String) marca[3],
                (String) marca[4]
            ));
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(instrucao, BorderLayout.NORTH);
        wrapper.add(grade,     BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private JPanel criarCardMarca(String sigla, Color bgLogo, Color fgLogo,
                                   String nome, String fileKey) {
        final ImageIcon logoIcon = carregarLogoMarca(fileKey);

        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setLayout(new GridBagLayout());
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        selecionarMarca(nome);
                        if (e.getClickCount() == 2) navegarParaModelo();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = nome.equals(marcaSelecionada);
                g2.setColor(sel ? new Color(255, 250, 240) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                Color bdrColor = sel   ? MainFrame.COR_GOLD
                               : hover ? new Color(0xc9a86c)
                                       : new Color(0xd0c9b8);
                g2.setColor(bdrColor);
                g2.setStroke(new BasicStroke(sel ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setPreferredSize(new Dimension(110, 110));

        JLabel lblLogo;
        if (logoIcon != null) {
            lblLogo = new JLabel(logoIcon, SwingConstants.CENTER);
        } else {
            lblLogo = new JLabel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(bgLogo);
                    g2.fill(new Ellipse2D.Float(0, 0, 80, 80));
                    g2.setColor(fgLogo);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, sigla.length() > 2 ? 14 : 18));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(sigla,
                        (80 - fm.stringWidth(sigla)) / 2,
                        (80 + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            };
            lblLogo.setPreferredSize(new Dimension(80, 80));
            lblLogo.setMinimumSize(new Dimension(80, 80));
            lblLogo.setMaximumSize(new Dimension(80, 80));
        }

        card.add(lblLogo);

        return card;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────────
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblMarcaSel = new JLabel("Nenhuma marca selecionada");
        lblMarcaSel.setFont(MainFrame.FONT_NORMAL);
        lblMarcaSel.setForeground(new Color(0x888888));

        btnProximo = new JButton("Próximo: selecionar modelo") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = !isEnabled() ? new Color(0xcccccc)
                         : getModel().isRollover() ? new Color(0x223060)
                         : MainFrame.COR_NAVY;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isEnabled() ? MainFrame.COR_GOLD : new Color(0x999999));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnProximo.setPreferredSize(new Dimension(260, 36));
        btnProximo.setOpaque(false);
        btnProximo.setContentAreaFilled(false);
        btnProximo.setBorderPainted(false);
        btnProximo.setFocusPainted(false);
        btnProximo.setEnabled(false);
        btnProximo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProximo.addActionListener(e -> navegarParaModelo());

        rodape.add(lblMarcaSel, BorderLayout.WEST);
        rodape.add(btnProximo,  BorderLayout.EAST);
        return rodape;
    }

    // ── Lógica ────────────────────────────────────────────────────────────────
    private void selecionarMarca(String nome) {
        marcaSelecionada = nome;
        lblMarcaSel.setText("Marca selecionada: " + nome);
        lblMarcaSel.setForeground(MainFrame.COR_NAVY);
        btnProximo.setEnabled(true);
        btnProximo.setText("Próximo: modelos de " + nome);
        repaint();
    }

    private void navegarParaModelo() {
        if (marcaSelecionada == null) return;
        frame.setMarcaSelecionada(marcaSelecionada);
        PanelSelecaoModelo.setMarcaSelecionada(marcaSelecionada, modoNovaOS);
        frame.mostrarTela(MainFrame.TELA_MODELO);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private ImageIcon carregarLogoMarca(String fileKey) {
        String caminho = "/images/logo" + fileKey + ".png";
        java.net.URL url = getClass().getResource(caminho);
        if (url != null) {
            try {
                BufferedImage bi = ImageIO.read(url);
                if (bi != null) {
                    int iw = bi.getWidth(), ih = bi.getHeight();
                    int newW, newH;
                    if (iw >= ih) { newW = 80; newH = Math.max(1, (int)(80.0 * ih / iw)); }
                    else          { newH = 80; newW = Math.max(1, (int)(80.0 * iw / ih)); }
                    return new ImageIcon(bi.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
                }
            } catch (java.io.IOException ignored) {}
        }
        return null;
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
