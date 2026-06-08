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

public class PanelCadastroCliente extends JPanel {

    private final MainFrame frame;

    private boolean isPessoaFisica = true;

    private JPanel painelCamposPF;
    private JPanel painelCamposPJ;
    private JPanel painelCentral;

    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtCpf;
    private JTextField txtRg;
    private JTextField txtDataNasc;
    private JTextField txtCnpj;
    private JTextField txtRazaoSocial;
    private JTextField txtNomeFantasia;
    private JTextField txtDataAbertura;

    public PanelCadastroCliente(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(),   BorderLayout.NORTH);
        add(criarCorpo(),    BorderLayout.CENTER);
        add(criarRodape(),   BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Cadastro de cliente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JScrollPane criarCorpo() {
        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel togglePanel = criarToggleTipo();
        togglePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(togglePanel);
        corpo.add(Box.createVerticalStrut(16));

        JPanel dadosPessoais = criarCamposDadosPessoais();
        dadosPessoais.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDadosPessoais = new JLabel("Dados pessoais");
        lblDadosPessoais.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDadosPessoais.setForeground(new Color(0x555555));
        lblDadosPessoais.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(lblDadosPessoais);
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(dadosPessoais);
        corpo.add(Box.createVerticalStrut(14));

        JLabel lblDadosEsp = new JLabel("Dados específicos");
        lblDadosEsp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDadosEsp.setForeground(new Color(0x555555));
        lblDadosEsp.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(lblDadosEsp);
        corpo.add(Box.createVerticalStrut(8));

        painelCamposPF = criarCamposPF();
        painelCamposPF.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCamposPJ = criarCamposPJ();
        painelCamposPJ.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCamposPJ.setVisible(false);

        corpo.add(painelCamposPF);
        corpo.add(painelCamposPJ);

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarToggleTipo() {
        JPanel toggle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toggle.setOpaque(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPF = criarBotaoTipo("Pessoa Física", true);
        JButton btnPJ = criarBotaoTipo("Pessoa Jurídica", false);

        btnPF.addActionListener(e -> {
            isPessoaFisica = true;
            painelCamposPF.setVisible(true);
            painelCamposPJ.setVisible(false);
            toggle.revalidate();
            toggle.repaint();
            repaint();
        });

        btnPJ.addActionListener(e -> {
            isPessoaFisica = false;
            painelCamposPF.setVisible(false);
            painelCamposPJ.setVisible(true);
            toggle.revalidate();
            toggle.repaint();
            repaint();
        });

        toggle.add(btnPF);
        toggle.add(btnPJ);
        return toggle;
    }

    private JButton criarBotaoTipo(String label, boolean isPF) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (isPF == isPessoaFisica);
                g2.setColor(ativo ? MainFrame.COR_NAVY : new Color(0xeeeeee));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ativo ? MainFrame.COR_GOLD : new Color(0x666666));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(140, 34));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel criarSecao(String titulo, JPanel conteudo) {
        JPanel secao = new JPanel(new BorderLayout());
        secao.setOpaque(false);
        secao.setAlignmentX(Component.LEFT_ALIGNMENT);
        secao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(new Color(0x555555));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));

        secao.add(lblTitulo, BorderLayout.NORTH);
        secao.add(conteudo,  BorderLayout.CENTER);
        return secao;
    }

    private JPanel criarCamposDadosPessoais() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 14, 10));
        painel.setOpaque(false);

        txtNome     = criarInput("Nome completo");
        txtTelefone = criarInput("(00) 00000-0000");
        txtEmail    = criarInput("exemplo@email.com");
        txtEndereco = criarInput("Rua, número, bairro, cidade");

        painel.add(criarGrupoCampo("Nome completo",  txtNome));
        painel.add(criarGrupoCampo("Telefone",       txtTelefone));
        painel.add(criarGrupoCampo("E-mail",         txtEmail));
        painel.add(criarGrupoCampo("Endereço",       txtEndereco));
        return painel;
    }

    private JPanel criarCamposPF() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 14, 10));
        painel.setOpaque(false);

        txtCpf      = criarInput("000.000.000-00");
        txtRg       = criarInput("00.000.000-0");
        txtDataNasc = criarInput("DD/MM/AAAA");

        painel.add(criarGrupoCampo("CPF",              txtCpf));
        painel.add(criarGrupoCampo("RG",               txtRg));
        painel.add(criarGrupoCampo("Data de nascimento", txtDataNasc));
        return painel;
    }

    private JPanel criarCamposPJ() {
        JPanel painel = new JPanel(new GridLayout(2, 2, 14, 10));
        painel.setOpaque(false);

        txtCnpj         = criarInput("00.000.000/0000-00");
        txtRazaoSocial  = criarInput("Razão social da empresa");
        txtNomeFantasia = criarInput("Nome fantasia (opcional)");
        txtDataAbertura = criarInput("DD/MM/AAAA");

        painel.add(criarGrupoCampo("CNPJ",           txtCnpj));
        painel.add(criarGrupoCampo("Razão social",   txtRazaoSocial));
        painel.add(criarGrupoCampo("Nome fantasia",  txtNomeFantasia));
        painel.add(criarGrupoCampo("Data de abertura", txtDataAbertura));
        return painel;
    }

    private JPanel criarGrupoCampo(String label, JTextField campo) {
        JPanel grupo = new JPanel();
        grupo.setOpaque(false);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(4));
        grupo.add(campo);
        return grupo;
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
                    g2.drawString(placeholder, 10, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
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

        JButton btnSalvar = new JButton("Salvar cliente") {
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
