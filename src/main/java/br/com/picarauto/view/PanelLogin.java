package br.com.picarauto.view;

/**
 * Tela de login. Sem sidebar.
 * Card centralizado (320×400px), fundo cream, sem header navy.
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
        setBackground(MainFrame.COR_CREAM);
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(320, 480));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 36, 28, 36));

        // --- Logo ---
        JLabel logoLabel = carregarLogo();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAutoCenter = new JLabel("AUTO CENTER", SwingConstants.CENTER);
        lblAutoCenter.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblAutoCenter.setForeground(MainFrame.COR_MUTED);
        lblAutoCenter.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divisor gold 40px
        JPanel divider = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(MainFrame.COR_GOLD);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        divider.setOpaque(false);
        divider.setMaximumSize(new Dimension(40, 2));
        divider.setPreferredSize(new Dimension(40, 2));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Campos ---
        JLabel lblUsuarioLabel = criarLabel("Usuário");
        lblUsuarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = criarCampo();
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel lblSenhaLabel = criarLabel("Senha");
        lblSenhaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSenha = new JPasswordField();
        estilizarInput(txtSenha);
        txtSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtSenha.addActionListener(e -> realizarLogin());

        lblErro = new JLabel(" ", SwingConstants.CENTER);
        lblErro.setFont(MainFrame.FONT_SMALL);
        lblErro.setForeground(new Color(0xcc2222));
        lblErro.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Botão Entrar ---
        JButton btnEntrar = new JButton("Entrar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
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
        btnEntrar.setOpaque(true);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.setPreferredSize(new Dimension(248, 40));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrar.addActionListener(e -> realizarLogin());

        // --- Montagem ---
        card.add(logoLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(lblAutoCenter);
        card.add(Box.createVerticalStrut(10));
        card.add(divider);
        card.add(Box.createVerticalStrut(28));
        card.add(lblUsuarioLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(txtUsuario);
        card.add(Box.createVerticalStrut(14));
        card.add(lblSenhaLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(txtSenha);
        card.add(Box.createVerticalStrut(10));
        card.add(lblErro);
        card.add(Box.createVerticalStrut(6));
        card.add(btnEntrar);

        add(card);
    }

    private JLabel carregarLogo() {
        // Tenta os dois nomes possíveis (Windows salva JFIF com extensão dupla)
        String[] caminhos = {"/images/logo.png", "/images/logo.png.jfif"};
        for (String caminho : caminhos) {
            java.net.URL url = getClass().getResource(caminho);
            System.out.println("Logo URL [" + caminho + "]: " + url);
            if (url != null) {
                Image scaled = new ImageIcon(url).getImage()
                    .getScaledInstance(260, 130, Image.SCALE_SMOOTH);
                return new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
            }
        }
        // Fallback via classloader (sem barra inicial)
        String[] caminhosCl = {"images/logo.png", "images/logo.png.jfif"};
        for (String caminho : caminhosCl) {
            try (java.io.InputStream stream =
                    getClass().getClassLoader().getResourceAsStream(caminho)) {
                System.out.println("Logo stream [" + caminho + "]: " + stream);
                if (stream != null) {
                    byte[] bytes = stream.readAllBytes();
                    Image scaled = new ImageIcon(bytes).getImage()
                        .getScaledInstance(260, 130, Image.SCALE_SMOOTH);
                    return new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
                }
            } catch (java.io.IOException ignored) {}
        }
        JLabel lbl = new JLabel("AV CAR", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(MainFrame.COR_NAVY);
        return lbl;
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        return lbl;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField();
        estilizarInput(f);
        return f;
    }

    private void estilizarInput(JTextField f) {
        f.setFont(MainFrame.FONT_NORMAL);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(248, 38));
    }

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
