package br.com.picarauto.view;

/**
 * Tela splash — exibida 3 segundos antes do login. Sem sidebar.
 * Fundo navy, logo gold, barra de progresso gold animada, navegação automática.
 *
 * @author Cassiano
 */
import javax.swing.*;
import java.awt.*;

public class PanelSplash extends JPanel {

    private final MainFrame frame;

    public PanelSplash(MainFrame frame) {
        this.frame = frame;
        setBackground(new Color(0x1a2744));
        setLayout(new GridBagLayout());
        construirUI();
    }

    private void construirUI() {
        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JLabel lblAV = new JLabel("AV CAR", SwingConstants.CENTER);
        lblAV.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblAV.setForeground(new Color(0xc9a86c));
        lblAV.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("AUTO CENTER — Sistema de Gestão", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(0x8899bb));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar(0, 100) {
            @Override public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x223060));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                int fillW = (int) (getWidth() * getValue() / 100.0);
                if (fillW > 0) {
                    g2.setColor(new Color(0xc9a86c));
                    g2.fillRoundRect(0, 0, fillW, getHeight(), 4, 4);
                }
                g2.dispose();
            }
        };
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);
        progressBar.setPreferredSize(new Dimension(200, 4));
        progressBar.setMaximumSize(new Dimension(200, 4));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        progressBar.setValue(0);

        JLabel lblRodape = new JLabel("SENAI FATESG · Projeto Integrador 2026/1", SwingConstants.CENTER);
        lblRodape.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRodape.setForeground(new Color(0x445577));
        lblRodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudo.add(lblAV);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(lblSub);
        conteudo.add(Box.createVerticalStrut(32));
        conteudo.add(progressBar);
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(lblRodape);

        add(conteudo);

        // Timer de navegação: 3000ms dispara uma vez e vai para LOGIN
        Timer timerNav = new Timer(3000, e -> frame.mostrarTela(MainFrame.TELA_LOGIN));
        timerNav.setRepeats(false);
        timerNav.start();

        // Timer de progresso: 30ms × 100 ticks = 3000ms, incrementa progressBar
        final int[] valor = {0};
        Timer progresso = new Timer(30, null);
        progresso.addActionListener(e -> {
            valor[0] += 1;
            progressBar.setValue(valor[0]);
            if (valor[0] >= 100) progresso.stop();
        });
        progresso.start();
    }
}
