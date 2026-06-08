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

public class PanelSplash extends JPanel {

    private final MainFrame frame;

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

        JPanel logoPanel = criarLogo();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("AV CAR AUTO CENTER");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(MainFrame.COR_CREAM);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Controle de Ordens de Serviço");
        lblSubtitulo.setFont(MainFrame.FONT_NORMAL);
        lblSubtitulo.setForeground(MainFrame.COR_MUTED);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEntrar = criarBotaoGold("Entrar no sistema");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        JLabel lblRodape = new JLabel("SENAI FATESG · Projeto Integrador 2026/1");
        lblRodape.setFont(MainFrame.FONT_SMALL);
        lblRodape.setForeground(new Color(0x445577));
        lblRodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudo.add(logoPanel);
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(lblSubtitulo);
        conteudo.add(Box.createVerticalStrut(32));
        conteudo.add(btnEntrar);
        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(lblRodape);

        add(conteudo);
    }

    private JPanel criarLogo() {
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(90, 90));
                setMaximumSize(new Dimension(90, 90));
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_GOLD);
                g2.fill(new RoundRectangle2D.Float(5, 5, 80, 80, 18, 18));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                String texto = "AV";
                int x = (90 - fm.stringWidth(texto)) / 2;
                int y = (90 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(texto, x, y);
                g2.dispose();
            }
        };
    }

    private JButton criarBotaoGold(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(0xdfc07e));
                } else {
                    g2.setColor(MainFrame.COR_GOLD);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
