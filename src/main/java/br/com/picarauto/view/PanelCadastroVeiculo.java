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

public class PanelCadastroVeiculo extends JPanel {

    private final MainFrame frame;

    private JTextField txtPlaca;
    private JTextField txtCor;
    private JTextField txtChassi;
    private JComboBox<String> cmbMarca;
    private JComboBox<String> cmbModelo;
    private JTextField txtCliente;

    private static final String[] MARCAS = {
        "Selecione a marca...", "Chevrolet", "Volkswagen", "Fiat", "Ford",
        "Toyota", "Honda", "Hyundai", "Renault", "Nissan", "Mercedes", "BMW", "Jeep"
    };

    private static final String[][] MODELOS = {
        {},
        {"Onix", "Tracker", "Cruze", "S10", "Spin"},
        {"Gol", "Polo", "T-Cross", "Virtus", "Nivus"},
        {"Argo", "Pulse", "Cronos", "Toro", "Strada"},
        {"Ka", "EcoSport", "Ranger", "Territory", "Bronco"},
        {"Corolla", "Hilux", "SW4", "Yaris", "RAV4"},
        {"Civic", "HR-V", "City", "Fit", "CR-V"},
        {"HB20", "Creta", "Tucson", "Santa Fe", "Elantra"},
        {"Kwid", "Sandero", "Logan", "Duster", "Captur"},
        {"Kicks", "Frontier", "Versa", "Sentra", "X-Trail"},
        {"Classe A", "Classe C", "GLA", "GLC", "Sprinter"},
        {"Serie 3", "Serie 5", "X1", "X3", "X5"},
        {"Renegade", "Compass", "Commander", "Wrangler", "Gladiator"},
    };

    public PanelCadastroVeiculo(MainFrame frame) {
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

        JLabel lblTitulo = new JLabel("Cadastro de veículo");
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
        corpo.setBorder(new EmptyBorder(20, 20, 20, 20));

        corpo.add(criarLabelSecao("Identificação do veículo"));
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(criarGridCampos(new JPanel[]{
            criarGrupoCampo("Placa",  txtPlaca  = criarInput("ABC-1234")),
            criarGrupoCampo("Cor",    txtCor    = criarInput("Branco, Prata, Preto...")),
            criarGrupoCampo("Chassi", txtChassi = criarInput("9BWZZZ377VT004251")),
            criarGrupoCampo("Cliente vinculado", txtCliente = criarInput("Nome do cliente")),
        }, 2));

        corpo.add(Box.createVerticalStrut(20));
        corpo.add(criarLabelSecao("Marca e modelo"));
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(criarGridMarcaModelo());

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JLabel criarLabelSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarGridCampos(JPanel[] grupos, int colunas) {
        JPanel grid = new JPanel(new GridLayout(
            (int) Math.ceil(grupos.length / (double) colunas), colunas, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (JPanel g : grupos) grid.add(g);
        return grid;
    }

    private JPanel criarGridMarcaModelo() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbMarca  = criarComboBox(MARCAS);
        cmbModelo = criarComboBox(new String[]{"Selecione primeiro a marca..."});
        cmbModelo.setEnabled(false);

        cmbMarca.addActionListener(e -> {
            int idx = cmbMarca.getSelectedIndex();
            cmbModelo.removeAllItems();
            if (idx > 0 && idx < MODELOS.length) {
                cmbModelo.setEnabled(true);
                cmbModelo.addItem("Selecione o modelo...");
                for (String m : MODELOS[idx]) cmbModelo.addItem(m);
            } else {
                cmbModelo.setEnabled(false);
                cmbModelo.addItem("Selecione primeiro a marca...");
            }
        });

        grid.add(criarGrupoCombo("Marca", cmbMarca));
        grid.add(criarGrupoCombo("Modelo", cmbModelo));
        return grid;
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

    private JPanel criarGrupoCombo(String label, JComboBox<String> combo) {
        JPanel grupo = new JPanel();
        grupo.setOpaque(false);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        combo.setFont(MainFrame.FONT_NORMAL);

        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(4));
        grupo.add(combo);
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

    private JComboBox<String> criarComboBox(String[] itens) {
        JComboBox<String> combo = new JComboBox<>(itens);
        combo.setFont(MainFrame.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(0xd0cbc0), 1));
        combo.setPreferredSize(new Dimension(0, 34));
        return combo;
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

        JButton btnSalvar = new JButton("Salvar veículo") {
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
