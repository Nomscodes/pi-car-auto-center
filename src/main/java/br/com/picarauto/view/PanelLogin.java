package br.com.picarauto.view;

/**
 * Tela de login — layout split navy (esquerda) / cream (direita).
 * Sem sidebar. Painel centralizado na janela com GridBagLayout.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelLogin extends JPanel {

    private final MainFrame frame;
    private JTextField     txtUsuario;
    private JPasswordField txtSenha;
    private JLabel         lblErro;

    public PanelLogin(MainFrame frame) {
        this.frame = frame;
        setBackground(new Color(0xeae5d8));
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel split = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.dispose();
            }
        };
        split.setOpaque(false);
        split.setPreferredSize(new Dimension(760, 480));

        split.add(criarPainelEsquerdo(), BorderLayout.WEST);
        split.add(criarPainelDireito(),  BorderLayout.CENTER);

        add(split);
    }

    // ── Painel esquerdo ───────────────────────────────────────────────────────
    private JPanel criarPainelEsquerdo() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Arredondamento só nos cantos esquerdo (top-left e bottom-left)
                g2.setColor(new Color(0x1a2744));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(320, 480));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(40, 32, 32, 32));

        // Badge
        JPanel badge = criarBadge("● Sistema de Gestão");
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo
        JLabel logoLabel = carregarLogo(280, 140);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline linha 1
        JLabel lblNome = new JLabel("AV CAR AUTO CENTER", SwingConstants.CENTER);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNome.setForeground(new Color(0xc9a86c));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline linha 2
        JLabel lblSub = new JLabel("Gestão completa de ordens de serviço", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(0x8899bb));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(badge);
        p.add(Box.createVerticalStrut(24));
        p.add(logoLabel);
        p.add(Box.createVerticalStrut(16));
        p.add(lblNome);
        p.add(Box.createVerticalStrut(6));
        p.add(lblSub);
        p.add(Box.createVerticalGlue());
        p.add(criarListrasDecorativas());

        return p;
    }

    private JPanel criarBadge(String texto) {
        JPanel badge = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(201, 168, 108, 38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0xc9a86c));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badge.setBorder(new EmptyBorder(5, 14, 5, 14));

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0xc9a86c));
        badge.add(lbl);

        // Forçar tamanho preferido após adicionar o label
        badge.setMaximumSize(new Dimension(220, 30));
        return badge;
    }

    private JPanel criarListrasDecorativas() {
        JPanel listras = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        listras.setOpaque(false);
        listras.setMaximumSize(new Dimension(320, 8));
        listras.setAlignmentX(Component.CENTER_ALIGNMENT);

        int[][] specs = {
            {40, 0x2d4a3e, 0},
            {28, 0x8899bb, 0},
            {36, 0xc9a86c, 0},
            {28, 0x8899bb, 0},
            {44, 0x1a2744, 1},
        };
        for (int[] spec : specs) {
            final int cor     = spec[1];
            final boolean brd = spec[2] == 1;
            JPanel listra = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(cor));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 3, 3);
                    if (brd) {
                        g2.setColor(new Color(0x8899bb));
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 3, 3);
                    }
                    g2.dispose();
                }
            };
            listra.setOpaque(false);
            listra.setPreferredSize(new Dimension(spec[0], 3));
            listras.add(listra);
        }
        return listras;
    }

    // ── Painel direito ────────────────────────────────────────────────────────
    private JPanel criarPainelDireito() {
        JPanel p = new JPanel();
        p.setBackground(new Color(0xf5f0e6));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTitulo = new JLabel("Bem-vindo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0x1a2744));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Faça login para acessar o sistema");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(0x8899bb));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel divider = new JPanel();
        divider.setBackground(new Color(0xc9a86c));
        divider.setMaximumSize(new Dimension(32, 2));
        divider.setPreferredSize(new Dimension(32, 2));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsuarioLabel = criarLabel("USUÁRIO");
        lblUsuarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        estilizarInput(txtUsuario);
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsuario.addActionListener(e -> realizarLogin());

        JLabel lblSenhaLabel = criarLabel("SENHA");
        lblSenhaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSenha = new JPasswordField();
        estilizarInput(txtSenha);
        txtSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSenha.addActionListener(e -> realizarLogin());

        lblErro = new JLabel(" ");
        lblErro.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblErro.setForeground(new Color(0xcc2222));
        lblErro.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnEntrar = criarBotaoEntrar();
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEsqueceu = new JLabel("Esqueceu a senha?", SwingConstants.CENTER);
        lblEsqueceu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEsqueceu.setForeground(new Color(0x8899bb));
        lblEsqueceu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEsqueceu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        p.add(lblTitulo);
        p.add(Box.createVerticalStrut(4));
        p.add(lblSubtitulo);
        p.add(Box.createVerticalStrut(24));
        p.add(divider);
        p.add(Box.createVerticalStrut(24));
        p.add(lblUsuarioLabel);
        p.add(Box.createVerticalStrut(6));
        p.add(txtUsuario);
        p.add(Box.createVerticalStrut(16));
        p.add(lblSenhaLabel);
        p.add(Box.createVerticalStrut(6));
        p.add(txtSenha);
        p.add(Box.createVerticalStrut(8));
        p.add(lblErro);
        p.add(Box.createVerticalStrut(8));
        p.add(btnEntrar);
        p.add(Box.createVerticalStrut(16));
        p.add(lblEsqueceu);
        p.add(Box.createVerticalGlue());

        return p;
    }

    // BOTÃO TELA LOGIN DE ENTRAR NO SISTEMA
    private JButton criarBotaoEntrar() {
        JButton btn = new JButton("Entrar →") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : new Color(0x1a2744));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setPreferredSize(new Dimension(300, 44));
        btn.addActionListener(e -> realizarLogin());
        return btn;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        return lbl;
    }

    private void estilizarInput(JTextField f) {
        f.setFont(MainFrame.FONT_NORMAL);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0c9b8), 1),
            new EmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        f.setPreferredSize(new Dimension(300, 40));
    }

    private JLabel carregarLogo(int w, int h) {
        String[] caminhos = {"/images/logo.png", "/images/logo.png.jfif"};
        for (String caminho : caminhos) {
            java.net.URL url = getClass().getResource(caminho);
            System.out.println("Logo URL [" + caminho + "]: " + url);
            if (url != null) {
                Image scaled = new ImageIcon(url).getImage()
                    .getScaledInstance(w, h, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
            }
        }
        String[] caminhosCl = {"images/logo.png", "images/logo.png.jfif"};
        for (String caminho : caminhosCl) {
            try (java.io.InputStream stream =
                    getClass().getClassLoader().getResourceAsStream(caminho)) {
                System.out.println("Logo stream [" + caminho + "]: " + stream);
                if (stream != null) {
                    byte[] bytes = stream.readAllBytes();
                    Image scaled = new ImageIcon(bytes).getImage()
                        .getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
                }
            } catch (java.io.IOException ignored) {}
        }
        JLabel lbl = new JLabel("AV CAR", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(new Color(0xc9a86c));
        return lbl;
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha   = new String(txtSenha.getPassword());
        if (usuario.equals("admin") && senha.equals("admin")) {
            MainFrame.setUsuarioLogado("Godofredo Silva");
            lblErro.setText(" ");
            frame.mostrarTela(MainFrame.TELA_SPLASH);
            Timer t = new Timer(2000, e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));
            t.setRepeats(false);
            t.start();
        } else {
            lblErro.setText("Usuário ou senha incorretos.");
            txtSenha.setText("");
        }
    }
}
