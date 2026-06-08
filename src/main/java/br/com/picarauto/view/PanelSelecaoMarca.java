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

public class PanelSelecaoMarca extends JPanel {

    private final MainFrame frame;

    private String marcaSelecionada = null;
    private JButton btnProximo;
    private JLabel lblMarcaSel;

    private static final Object[][] MARCAS = {
        {"GM",  new Color(0xFFD700), new Color(0x1a1a1a), "Chevrolet"},
        {"VW",  new Color(0x001e50), Color.WHITE,          "Volkswagen"},
        {"FI",  new Color(0xc8102e), Color.WHITE,          "Fiat"},
        {"FO",  new Color(0x003da5), Color.WHITE,          "Ford"},
        {"TO",  new Color(0xeb0a1e), Color.WHITE,          "Toyota"},
        {"HO",  new Color(0xe40521), Color.WHITE,          "Honda"},
        {"HY",  new Color(0x002c5f), Color.WHITE,          "Hyundai"},
        {"RN",  new Color(0xefdf00), new Color(0x333333),  "Renault"},
        {"NI",  new Color(0xc3002f), Color.WHITE,          "Nissan"},
        {"MB",  new Color(0x1c1c1c), new Color(0xc0c0c0),  "Mercedes"},
        {"BMW", new Color(0x0066b2), Color.WHITE,          "BMW"},
        {"JE",  new Color(0x006a4e), Color.WHITE,          "Jeep"},
    };

    public PanelSelecaoMarca(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(), BorderLayout.NORTH);
        add(criarGrade(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Selecionar marca do veículo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JScrollPane criarGrade() {
        JPanel instrucao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instrucao.setOpaque(false);
        instrucao.setBorder(new EmptyBorder(12, 20, 0, 20));
        JLabel lblInstrucao = new JLabel("Clique na marca do veículo:");
        lblInstrucao.setFont(MainFrame.FONT_NORMAL);
        lblInstrucao.setForeground(new Color(0x555555));
        instrucao.add(lblInstrucao);

        JPanel grade = new JPanel(new GridLayout(3, 4, 14, 14));
        grade.setOpaque(false);
        grade.setBorder(new EmptyBorder(8, 20, 8, 20));

        for (Object[] marca : MARCAS) {
            grade.add(criarCardMarca(
                (String) marca[0],
                (Color)  marca[1],
                (Color)  marca[2],
                (String) marca[3]
            ));
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(instrucao, BorderLayout.NORTH);
        wrapper.add(grade, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private JPanel criarCardMarca(String sigla, Color bgLogo, Color fgLogo, String nome) {
        JPanel card = new JPanel() {
            private boolean hover = false;
            private boolean selecionado = false;

            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setLayout(new GridBagLayout());
                putClientProperty("nome", nome);

                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        selecionarMarca(nome, this_());
                    }
                });
            }

            private JPanel this_() { return this; }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean sel = nome.equals(marcaSelecionada);
                Color bg    = sel ? new Color(0xfffbf4) : Color.WHITE;
                Color borda = sel ? MainFrame.COR_GOLD : (hover ? MainFrame.COR_NAVY : new Color(0xe0dbd0));
                float espessura = sel ? 2.5f : (hover ? 1.5f : 0.5f);

                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(borda);
                g2.setStroke(new BasicStroke(espessura));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JPanel logo = criarLogo(sigla, bgLogo, fgLogo);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNome.setForeground(new Color(0x333333));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(logo);
        inner.add(Box.createVerticalStrut(8));
        inner.add(lblNome);

        card.add(inner);
        return card;
    }

    private JPanel criarLogo(String sigla, Color bg, Color fg) {
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(54, 54));
                setMaximumSize(new Dimension(54, 54));
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(2, 2, 50, 50);
                g2.setColor(fg);
                g2.setFont(new Font("Segoe UI", Font.BOLD, sigla.length() > 2 ? 11 : 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (54 - fm.stringWidth(sigla)) / 2;
                int y = (54 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(sigla, x, y);
                g2.dispose();
            }
        };
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblMarcaSel = new JLabel("Nenhuma marca selecionada");
        lblMarcaSel.setFont(MainFrame.FONT_NORMAL);
        lblMarcaSel.setForeground(new Color(0x888888));

        btnProximo = new JButton("Próximo: selecionar modelo") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = !isEnabled() ? new Color(0xcccccc)
                         : getModel().isRollover() ? new Color(0x223060)
                         : MainFrame.COR_NAVY;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(isEnabled() ? MainFrame.COR_GOLD : new Color(0x999999));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btnProximo.setPreferredSize(new Dimension(240, 36));
        btnProximo.setBorderPainted(false);
        btnProximo.setContentAreaFilled(false);
        btnProximo.setFocusPainted(false);
        btnProximo.setEnabled(false);
        btnProximo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProximo.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_COMPOSICAO));

        rodape.add(lblMarcaSel, BorderLayout.WEST);
        rodape.add(btnProximo,  BorderLayout.EAST);
        return rodape;
    }

    private void selecionarMarca(String nome, JPanel cardClicado) {
        marcaSelecionada = nome;
        lblMarcaSel.setText("Marca selecionada: " + nome);
        lblMarcaSel.setForeground(MainFrame.COR_NAVY);
        btnProximo.setEnabled(true);
        btnProximo.setText("Próximo: modelos de " + nome);
        repaint();
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
                g2.setFont(MainFrame.FONT_SMALL);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
