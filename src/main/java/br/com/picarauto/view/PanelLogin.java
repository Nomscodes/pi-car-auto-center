package br.com.picarauto.view;

/**
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class PanelLogin extends JPanel {

    private final MainFrame frame;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JLabel lblErro;

    public PanelLogin(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_NAVY);
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xf5f0e6));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(400, 420));

        // Logo
        JPanel logo = new JPanel() {
            { setOpaque(false); setPreferredSize(new Dimension(70, 70)); setMaximumSize(new Dimension(70, 70)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, 70, 70, 14, 14));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("AV", (70 - fm.stringWidth("AV")) / 2, (70 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("AV CAR AUTO CENTER");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(MainFrame.COR_NAVY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Acesso restrito a colaboradores");
        lblSub.setFont(MainFrame.FONT_SMALL);
        lblSub.setForeground(new Color(0x888888));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuário");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUsuario.setForeground(new Color(0x555555));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setFont(MainFrame.FONT_NORMAL);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSenha.setForeground(new Color(0x555555));
        lblSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSenha = new JPasswordField();
        txtSenha.setFont(MainFrame.FONT_NORMAL);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        txtSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtSenha.addActionListener(e -> realizarLogin());

        lblErro = new JLabel(" ");
        lblErro.setFont(MainFrame.FONT_SMALL);
        lblErro.setForeground(new Color(0xcc0000));
        lblErro.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEntrar = new JButton("Entrar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnEntrar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 42));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> realizarLogin());

        card.add(logo);
        card.add(Box.createVerticalStrut(16));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(28));
        card.add(lblUsuario);
        card.add(Box.createVerticalStrut(6));
        card.add(txtUsuario);
        card.add(Box.createVerticalStrut(14));
        card.add(lblSenha);
        card.add(Box.createVerticalStrut(6));
        card.add(txtSenha);
        card.add(Box.createVerticalStrut(8));
        card.add(lblErro);
        card.add(Box.createVerticalStrut(8));
        card.add(btnEntrar);
        card.add(Box.createVerticalStrut(16));

        JLabel lblRodape = new JLabel("SENAI FATESG · PI 2026/1");
        lblRodape.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRodape.setForeground(new Color(0x445577));
        lblRodape.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblRodape);

        add(card);
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha   = new String(txtSenha.getPassword());
        // Credenciais fixas para demonstração — integrar com backend no futuro
        if (usuario.equals("admin") && senha.equals("admin")) {
            lblErro.setText(" ");
            frame.mostrarTela(MainFrame.TELA_DASHBOARD);
        } else {
            lblErro.setText("Usuário ou senha incorretos.");
            txtSenha.setText("");
        }
    }
}
