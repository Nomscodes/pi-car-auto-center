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
    private JTextField     txtUsuario;
    private JPasswordField txtSenha;
    private JLabel         lblErro;

    public PanelLogin(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_NAVY);
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_CREAM);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(400, 460));

        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(32, 40, 32, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.weightx = 1.0;

        // Logo
        JLabel logo = new JLabel("AV") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, 80, 80, 16, 16));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("AV", (80-fm.stringWidth("AV"))/2, (80+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(80, 80));
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTitulo = new JLabel("AV CAR AUTO CENTER", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(MainFrame.COR_NAVY);

        JLabel lblSub = new JLabel("Acesso restrito a colaboradores", SwingConstants.CENTER);
        lblSub.setFont(MainFrame.FONT_SMALL);
        lblSub.setForeground(new Color(0x888888));

        JLabel lblUsuario = new JLabel("Usuário");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUsuario.setForeground(new Color(0x555555));

        txtUsuario = new JTextField();
        txtUsuario.setFont(MainFrame.FONT_NORMAL);
        txtUsuario.setPreferredSize(new Dimension(320, 38));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(6, 10, 6, 10)));

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSenha.setForeground(new Color(0x555555));

        txtSenha = new JPasswordField();
        txtSenha.setFont(MainFrame.FONT_NORMAL);
        txtSenha.setPreferredSize(new Dimension(320, 38));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtSenha.addActionListener(e -> realizarLogin());

        lblErro = new JLabel(" ", SwingConstants.CENTER);
        lblErro.setFont(MainFrame.FONT_SMALL);
        lblErro.setForeground(new Color(0xcc0000));

        JButton btnEntrar = new JButton("Entrar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btnEntrar.setPreferredSize(new Dimension(320, 42));
        btnEntrar.setBorderPainted(false);
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> realizarLogin());

        JLabel lblRodape = new JLabel("SENAI FATESG · PI 2026/1", SwingConstants.CENTER);
        lblRodape.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRodape.setForeground(new Color(0x888888));

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 12, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        corpo.add(logo, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 4, 0);
        corpo.add(lblTitulo, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 20, 0);
        corpo.add(lblSub, gbc);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        corpo.add(lblUsuario, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 12, 0);
        corpo.add(txtUsuario, gbc);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        corpo.add(lblSenha, gbc);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 6, 0);
        corpo.add(txtSenha, gbc);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        corpo.add(lblErro, gbc);
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 16, 0);
        corpo.add(btnEntrar, gbc);
        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 0, 0);
        corpo.add(lblRodape, gbc);

        card.add(corpo, BorderLayout.CENTER);
        add(card);
    }

    private void realizarLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha   = new String(txtSenha.getPassword());
        if (usuario.equals("admin") && senha.equals("admin")) {
            MainFrame.setUsuarioLogado("admin");
            lblErro.setText(" ");
            frame.mostrarTela(MainFrame.TELA_DASHBOARD);
        } else {
            lblErro.setText("Usuário ou senha incorretos.");
            txtSenha.setText("");
        }
    }
}
