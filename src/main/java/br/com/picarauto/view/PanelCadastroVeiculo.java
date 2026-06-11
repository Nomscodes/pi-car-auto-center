package br.com.picarauto.view;

/**
 * Formulário de cadastro de veículo — campos em grid 2 colunas.
 * Marca readonly (vinda da seleção anterior) e modelo readonly.
 * Lógica de cascata marca/modelo preservada via arrays estáticos.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelCadastroVeiculo extends JPanel {

    private final MainFrame frame;

    private JTextField    txtPlaca, txtCor, txtAno, txtRenavam, txtCliente;
    private JComboBox<String> cmbMarca, cmbModelo;

    private static final String[] MARCAS = {
        "Selecione...", "Chevrolet", "Volkswagen", "Fiat", "Ford",
        "Toyota", "Honda", "Hyundai", "Renault", "Nissan",
        "Jeep", "Peugeot", "Citroën", "Mitsubishi", "Kia", "Subaru", "Mercedes"
    };

    private static final String[][] MODELOS = {
        {},
        {"Onix", "Tracker", "Cruze", "S10", "Spin", "Montana"},
        {"Gol", "Polo", "T-Cross", "Virtus", "Nivus", "Amarok"},
        {"Argo", "Pulse", "Cronos", "Toro", "Strada", "Mobi"},
        {"Ka", "EcoSport", "Ranger", "Territory", "Bronco"},
        {"Corolla", "Hilux", "SW4", "Yaris", "RAV4"},
        {"Civic", "HR-V", "City", "Fit", "CR-V"},
        {"HB20", "Creta", "Tucson", "Santa Fe", "Elantra"},
        {"Kwid", "Sandero", "Logan", "Duster", "Captur"},
        {"Kicks", "Frontier", "Versa", "Sentra"},
        {"Renegade", "Compass", "Commander", "Wrangler"},
        {"208", "2008", "308", "3008", "5008"},
        {"C3", "C4 Cactus", "C5 Aircross"},
        {"Eclipse Cross", "Outlander", "L200"},
        {"Sportage", "Sorento", "Stinger", "Soul"},
        {"Impreza", "Forester", "Outback", "WRX"},
        {"Classe A", "Classe C", "GLA", "GLC", "Sprinter"},
    };

    public PanelCadastroVeiculo(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_VEICULO), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Cadastro de Veículo");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        bar.add(lbl,       BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JScrollPane criarScrollConteudo() {
        JPanel corpo = new JPanel();
        corpo.setBackground(MainFrame.COR_CREAM);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(new EmptyBorder(24, 24, 24, 24));

        corpo.add(criarLabelSecao("Identificação do veículo"));
        corpo.add(Box.createVerticalStrut(10));

        // Grid principal
        JPanel grid = new JPanel(new GridLayout(3, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPlaca   = criarCampo(); txtCor    = criarCampo();
        txtAno     = criarCampo(); txtRenavam = criarCampo();
        txtCliente = criarCampo();

        grid.add(criarGrupo("Placa",               txtPlaca));
        grid.add(criarGrupo("Cor",                 txtCor));
        grid.add(criarGrupo("Ano",                 txtAno));
        grid.add(criarGrupo("Renavam",             txtRenavam));
        grid.add(criarGrupo("Cliente proprietário", txtCliente));
        grid.add(new JPanel() {{ setOpaque(false); }});

        corpo.add(grid);
        corpo.add(Box.createVerticalStrut(20));

        // Marca / Modelo
        corpo.add(criarLabelSecao("Marca e modelo"));
        corpo.add(Box.createVerticalStrut(10));
        corpo.add(criarGridMarcaModelo());

        // Rodapé
        corpo.add(Box.createVerticalStrut(24));
        corpo.add(criarRodapeAcoes());

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarGridMarcaModelo() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbMarca  = criarCombo(MARCAS);
        cmbModelo = criarCombo(new String[]{"Selecione primeiro a marca..."});
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

        grid.add(criarGrupoCombo("Marca",  cmbMarca));
        grid.add(criarGrupoCombo("Modelo", cmbModelo));
        return grid;
    }

    // ── Rodapé de ações ───────────────────────────────────────────────────────
    private JPanel criarRodapeAcoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCancelar = criarBotaoOutline("Cancelar", 110, 36);
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        JButton btnSalvar = criarBotaoGold("Salvar veículo", 140, 36);

        p.add(btnCancelar);
        p.add(btnSalvar);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel criarLabelSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainFrame.COR_NAVY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarGrupo(String label, JTextField campo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(campo);
        return g;
    }

    private JPanel criarGrupoCombo(String label, JComboBox<String> combo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        combo.setFont(MainFrame.FONT_NORMAL);

        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(combo);
        return g;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField();
        f.setFont(MainFrame.FONT_NORMAL);
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34));
        return f;
    }

    private JComboBox<String> criarCombo(String[] itens) {
        JComboBox<String> c = new JComboBox<>(itens);
        c.setFont(MainFrame.FONT_NORMAL);
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(0, 34));
        return c;
    }

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setForeground(MainFrame.COR_NAVY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoGold(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
