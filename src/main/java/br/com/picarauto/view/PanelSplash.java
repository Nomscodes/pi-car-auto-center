package br.com.picarauto.view;

/**
 * Tela splash — exibida antes do login. Sem sidebar.
 * Fundo navy, logo gold centralizado, barra de progresso gold animada.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelSplash extends JPanel {

    private final MainFrame frame;
    private int   progresso = 0;
    private Timer timerAnim;

    public PanelSplash(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_NAVY);
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        // Logo
        JPanel logo = criarLogo();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAV = new JLabel("AV CAR", SwingConstants.CENTER);
        lblAV.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblAV.setForeground(MainFrame.COR_GOLD);
        lblAV.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("AUTO CENTER — Sistema de Gestão", SwingConstants.CENTER);
        lblSub.setFont(MainFrame.FONT_NORMAL);
        lblSub.setForeground(MainFrame.COR_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Barra de progresso customizada
        JPanel barraWrap = criarBarraProgressoWrap();
        barraWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão (aparece quando progresso completo)
        JButton btnEntrar = criarBotaoEntrar();
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setVisible(false);
        btnEntrar.addActionListener(e -> {
            if (timerAnim != null) timerAnim.stop();
            frame.mostrarTela(MainFrame.TELA_LOGIN);
        });

        JLabel lblRodape = new JLabel("SENAI FATESG · Projeto Integrador 2026/1", SwingConstants.CENTER);
        lblRodape.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRodape.setForeground(new Color(0x445577));
        lblRodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudo.add(logo);
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(lblAV);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(lblSub);
        conteudo.add(Box.createVerticalStrut(32));
        conteudo.add(barraWrap);
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(btnEntrar);
        conteudo.add(Box.createVerticalStrut(12));
        conteudo.add(lblRodape);

        add(conteudo);

        // Animar progresso e exibir botão ao completar
        timerAnim = new Timer(25, null);
        timerAnim.addActionListener(e -> {
            progresso = Math.min(100, progresso + 2);
            barraWrap.repaint();
            if (progresso >= 100) {
                timerAnim.stop();
                btnEntrar.setVisible(true);
            }
        });
        timerAnim.start();
    }

    private JPanel criarLogo() {
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(80, 80));
                setMaximumSize(new Dimension(80, 80));
            }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_GOLD);
                g2.fill(new RoundRectangle2D.Float(4, 4, 72, 72, 16, 16));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                String t = "AV";
                g2.drawString(t, (80 - fm.stringWidth(t)) / 2, (80 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
    }

    private JPanel criarBarraProgressoWrap() {
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(260, 6));
                setMaximumSize(new Dimension(260, 6));
            }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fundo semi-transparente
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                // Fill gold
                int fillW = (int) (getWidth() * progresso / 100.0);
                g2.setColor(MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, fillW, getHeight(), 4, 4);
                g2.dispose();
            }
        };
    }

    private JButton criarBotaoEntrar() {
        JButton btn = new JButton("Entrar no sistema") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
