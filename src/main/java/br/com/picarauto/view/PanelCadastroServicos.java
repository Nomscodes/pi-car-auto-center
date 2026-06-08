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

public class PanelCadastroServicos extends JPanel {

    private final MainFrame frame;

    private boolean abaInterno = true;

    private JPanel painelInterno;
    private JPanel painelExterno;
    private JTextField txtDescricaoInterno;
    private JTextField txtValorInterno;
    private JTextField txtDescricaoExterno;
    private JTextField txtValorExterno;

    public PanelCadastroServicos(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(),  BorderLayout.NORTH);
        add(criarCorpo(),   BorderLayout.CENTER);
        add(criarRodape(),  BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Cadastro de serviços");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setOpaque(false);

        corpo.add(criarToggleAbas(), BorderLayout.NORTH);

        painelInterno = criarPainelInterno();
        painelExterno = criarPainelExterno();
        painelExterno.setVisible(false);

        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setOpaque(false);
        conteudo.add(painelInterno, BorderLayout.CENTER);
        conteudo.add(painelExterno, BorderLayout.SOUTH);

        corpo.add(conteudo, BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarToggleAbas() {
        JPanel toggle = new JPanel(new BorderLayout());
        toggle.setBackground(MainFrame.COR_CREAM_ALT);
        toggle.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xd0cbc0)));

        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);
        abas.setBorder(new EmptyBorder(8, 20, 0, 20));

        JButton btnInterno = criarAba("Serviço interno", true);
        JButton btnExterno = criarAba("Serviço externo", false);

        btnInterno.addActionListener(e -> {
            abaInterno = true;
            painelInterno.setVisible(true);
            painelExterno.setVisible(false);
            abas.repaint();
        });

        btnExterno.addActionListener(e -> {
            abaInterno = false;
            painelInterno.setVisible(false);
            painelExterno.setVisible(true);
            abas.repaint();
        });

        abas.add(btnInterno);
        abas.add(btnExterno);
        toggle.add(abas, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(8, 0, 0, 20));

        JLabel lblInfo = new JLabel("Factory Method cria o tipo correto automaticamente");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(new Color(0xaaaaaa));
        infoPanel.add(lblInfo);
        toggle.add(infoPanel, BorderLayout.EAST);

        return toggle;
    }

    private JButton criarAba(String label, boolean isInterno) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                boolean ativo = (isInterno == abaInterno);
                if (ativo) {
                    g2.setColor(MainFrame.COR_CREAM);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(MainFrame.COR_NAVY);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
                }
                g2.setColor(ativo ? MainFrame.COR_NAVY : new Color(0x888888));
                g2.setFont(new Font("Segoe UI", ativo ? Font.BOLD : Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(160, 36));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel criarPainelInterno() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        painel.add(criarBadge("Serviço interno", new Color(0xcfe2ff), new Color(0x084298)));
        painel.add(Box.createVerticalStrut(16));

        painel.add(criarLabel("Descrição do serviço"));
        painel.add(Box.createVerticalStrut(6));
        txtDescricaoInterno = criarInput("Ex: Troca de óleo, Alinhamento, Freios...");
        txtDescricaoInterno.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDescricaoInterno.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        painel.add(txtDescricaoInterno);

        painel.add(Box.createVerticalStrut(14));
        painel.add(criarLabel("Valor cobrado (R$)"));
        painel.add(Box.createVerticalStrut(6));

        JPanel valorRow = new JPanel(new GridLayout(1, 2, 14, 0));
        valorRow.setOpaque(false);
        valorRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtValorInterno = criarInput("Ex: 150.00");
        valorRow.add(txtValorInterno);
        valorRow.add(new JPanel() {{ setOpaque(false); }});
        painel.add(valorRow);

        painel.add(Box.createVerticalStrut(20));
        painel.add(criarInfoExecucao());

        return painel;
    }

    private JPanel criarPainelExterno() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        painel.add(criarBadge("Serviço externo (terceirizado)", new Color(0xfff3cd), new Color(0x856404)));
        painel.add(Box.createVerticalStrut(16));

        painel.add(criarLabel("Descrição do serviço"));
        painel.add(Box.createVerticalStrut(6));
        txtDescricaoExterno = criarInput("Ex: Funilaria, Pintura, Tapeçaria...");
        txtDescricaoExterno.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDescricaoExterno.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        painel.add(txtDescricaoExterno);

        painel.add(Box.createVerticalStrut(14));
        painel.add(criarLabel("Valor cobrado (R$)"));
        painel.add(Box.createVerticalStrut(6));

        JPanel valorRow = new JPanel(new GridLayout(1, 2, 14, 0));
        valorRow.setOpaque(false);
        valorRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtValorExterno = criarInput("Ex: 800.00");
        valorRow.add(txtValorExterno);
        valorRow.add(new JPanel() {{ setOpaque(false); }});
        painel.add(valorRow);

        painel.add(Box.createVerticalStrut(20));
        painel.add(criarInfoTerceirizado());

        return painel;
    }

    private JPanel criarBadge(String texto, Color bg, Color fg) {
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setOpaque(false);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(fg);
        lbl.setBorder(new EmptyBorder(4, 12, 4, 12));
        badge.add(lbl);
        return badge;
    }

    private JPanel criarInfoExecucao() {
        return criarInfoBox(
            "Executado pela equipe interna",
            "Serviços internos são realizados pelos colaboradores da oficina.",
            "O colaborador responsável é vinculado na Ordem de Serviço.",
            new Color(0xeaf3de), new Color(0x27500a), new Color(0x3b6d11)
        );
    }

    private JPanel criarInfoTerceirizado() {
        return criarInfoBox(
            "Executado por empresa terceirizada",
            "Serviços externos são realizados por parceiros fora da oficina.",
            "O fornecedor é vinculado ao item de serviço externo na OS.",
            new Color(0xfff3cd), new Color(0x856404), new Color(0x664d03)
        );
    }

    private JPanel criarInfoBox(String titulo, String linha1, String linha2,
                                 Color bg, Color corTitulo, Color corTexto) {
        JPanel box = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(12, 16, 12, 16));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));

        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblT.setForeground(corTitulo);

        JLabel lbl1 = new JLabel(linha1);
        lbl1.setFont(MainFrame.FONT_SMALL);
        lbl1.setForeground(corTexto);

        JLabel lbl2 = new JLabel(linha2);
        lbl2.setFont(MainFrame.FONT_SMALL);
        lbl2.setForeground(corTexto);

        texto.add(lblT);
        texto.add(Box.createVerticalStrut(4));
        texto.add(lbl1);
        texto.add(lbl2);
        box.add(texto, BorderLayout.CENTER);
        return box;
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField criarInput(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(0xaaaaaa));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10,
                        getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(MainFrame.FONT_NORMAL);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(0, 34));
        return field;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xd0cbc0)));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(MainFrame.FONT_NORMAL);
        btnCancelar.setForeground(new Color(0x666666));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(0xbbbbbb), 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(100, 34));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        JButton btnSalvar = new JButton("Salvar serviço") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnSalvar.setPreferredSize(new Dimension(140, 34));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
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
