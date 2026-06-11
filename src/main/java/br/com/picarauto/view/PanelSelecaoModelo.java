package br.com.picarauto.view;

/**
 * Seleção de modelo do veículo — grid 4 colunas, mesma estética da tela de marcas.
 * Toda a lógica de carregarModelos(), setMarcaSelecionada() e modoNovaOS preservada.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class PanelSelecaoModelo extends JPanel {

    private final MainFrame frame;

    private static String  marcaAtual  = "";
    private static boolean modoNovaOS  = false;

    private String  modeloSelecionado  = null;
    private JButton btnProximo;
    private JLabel  lblModeloSel;
    private JLabel  lblTitulo;
    private JPanel  gradeModelos;

    private static final Map<String, String[]> MODELOS_POR_MARCA = new HashMap<>();
    static {
        MODELOS_POR_MARCA.put("Chevrolet",    new String[]{"Onix", "Tracker", "Cruze", "S10", "Spin", "Montana", "Onix Plus", "Equinox"});
        MODELOS_POR_MARCA.put("Volkswagen",   new String[]{"Gol", "Polo", "T-Cross", "Virtus", "Nivus", "Saveiro", "Tiguan", "Jetta"});
        MODELOS_POR_MARCA.put("Hyundai",      new String[]{"HB20", "Creta", "Tucson", "HB20S", "Santa Fe", "Azera", "i30", "Venue"});
        MODELOS_POR_MARCA.put("Toyota",       new String[]{"Corolla", "Hilux", "Yaris", "SW4", "RAV4", "Camry", "Etios", "Corolla Cross"});
        MODELOS_POR_MARCA.put("Ford",         new String[]{"Ka", "EcoSport", "Ranger", "Territory", "Bronco Sport", "Maverick", "Edge", "Fusion"});
        MODELOS_POR_MARCA.put("Fiat",         new String[]{"Argo", "Pulse", "Toro", "Strada", "Cronos", "Mobi", "Fastback", "Doblo"});
        MODELOS_POR_MARCA.put("Honda",        new String[]{"Civic", "HR-V", "CR-V", "Fit", "City", "WR-V", "Accord", "Pilot"});
        MODELOS_POR_MARCA.put("Renault",      new String[]{"Kwid", "Sandero", "Duster", "Logan", "Captur", "Oroch", "Master", "Zoe"});
        MODELOS_POR_MARCA.put("Nissan",       new String[]{"Kicks", "Frontier", "Versa", "Sentra", "March", "Leaf", "Murano", "Pathfinder"});
        MODELOS_POR_MARCA.put("Jeep",         new String[]{"Compass", "Renegade", "Commander", "Wrangler", "Gladiator", "Grand Cherokee"});
        MODELOS_POR_MARCA.put("Peugeot",      new String[]{"208", "2008", "3008", "408", "308", "Expert", "Landtrek", "e-208"});
        MODELOS_POR_MARCA.put("Citroën",      new String[]{"C3", "C4 Cactus", "Aircross", "Berlingo", "Jumper", "C5", "C4", "Spacetourer"});
        MODELOS_POR_MARCA.put("Mitsubishi",   new String[]{"Eclipse Cross", "Outlander", "L200", "ASX", "Pajero", "Galant"});
        MODELOS_POR_MARCA.put("Kia",          new String[]{"Sportage", "Sorento", "Stinger", "Carnival", "Picanto", "Soul", "Cerato", "EV6"});
        MODELOS_POR_MARCA.put("Subaru",       new String[]{"Forester", "Outback", "Impreza", "XV", "Legacy", "BRZ", "WRX", "Ascent"});
        MODELOS_POR_MARCA.put("Mercedes-Benz",new String[]{"Classe A", "Classe C", "GLA", "GLC", "Sprinter", "Actros", "CLA", "EQC"});
    }

    public static void setMarcaSelecionada(String marca, boolean novaOS) {
        marcaAtual = marca;
        modoNovaOS = novaOS;
    }

    public PanelSelecaoModelo(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollGrade(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_MODELO), BorderLayout.EAST);
        inner.add(criarRodape(), BorderLayout.SOUTH);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        lblTitulo = new JLabel("AV CAR AUTO CENTER  —  Selecionar Modelo — " + marcaAtual);
        lblTitulo.setName("lblTitulo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        bar.add(lblTitulo, BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Grade ─────────────────────────────────────────────────────────────────
    private JScrollPane criarScrollGrade() {
        JPanel instrucao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instrucao.setOpaque(false);
        instrucao.setBorder(new EmptyBorder(16, 20, 0, 20));
        JLabel lbl = new JLabel("Selecione o modelo:");
        lbl.setFont(MainFrame.FONT_NORMAL);
        lbl.setForeground(new Color(0x555555));
        instrucao.add(lbl);

        gradeModelos = new JPanel(new GridLayout(0, 4, 20, 20));
        gradeModelos.setOpaque(false);
        gradeModelos.setBorder(new EmptyBorder(12, 20, 12, 20));

        carregarModelos();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(instrucao,    BorderLayout.NORTH);
        wrapper.add(gradeModelos, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    /** Chamado por MainFrame.mostrarTela() toda vez que a tela é exibida. */
    public void carregarModelos() {
        if (lblTitulo != null)
            lblTitulo.setText("AV CAR AUTO CENTER  —  Selecionar Modelo — " + marcaAtual);

        modeloSelecionado = null;
        if (lblModeloSel != null) {
            lblModeloSel.setText("Nenhum modelo selecionado");
            lblModeloSel.setForeground(new Color(0x888888));
        }
        if (btnProximo != null) btnProximo.setEnabled(false);
        if (gradeModelos == null) return;

        gradeModelos.removeAll();
        String[]  modelos  = MODELOS_POR_MARCA.getOrDefault(marcaAtual, new String[]{});
        Color[]   corMarca = getMarcaCor(marcaAtual);

        for (String modelo : modelos)
            gradeModelos.add(criarCardModelo(modelo, corMarca[0], corMarca[1]));

        gradeModelos.revalidate();
        gradeModelos.repaint();
    }

    private Color[] getMarcaCor(String marca) {
        for (Object[] m : PanelSelecaoMarca.MARCAS)
            if (m[3].equals(marca)) return new Color[]{(Color) m[1], (Color) m[2]};
        return new Color[]{MainFrame.COR_NAVY, Color.WHITE};
    }

    private JPanel criarCardModelo(String nome, Color bgCor, Color fgCor) {
        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setLayout(new GridBagLayout());
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { selecionarModelo(nome); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = nome.equals(modeloSelecionado);
                int cx = getWidth() / 2, cy = getHeight() / 2 - 10;
                int r  = Math.min(Math.min(getWidth(), getHeight()) / 2 - 12, 40);

                if (sel) {
                    g2.setColor(new Color(201, 168, 108, 40));
                    g2.fill(new Ellipse2D.Float(cx - r - 6, cy - r - 6, (r + 6) * 2, (r + 6) * 2));
                    g2.setColor(MainFrame.COR_GOLD);
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.draw(new Ellipse2D.Float(cx - r - 4, cy - r - 4, (r + 4) * 2, (r + 4) * 2));
                } else if (hover) {
                    g2.setColor(new Color(26, 39, 68, 22));
                    g2.fill(new Ellipse2D.Float(cx - r - 4, cy - r - 4, (r + 4) * 2, (r + 4) * 2));
                }

                g2.setColor(sel ? bgCor : new Color(
                    bgCor.getRed(), bgCor.getGreen(), bgCor.getBlue(), 200));
                g2.fill(new Ellipse2D.Float(cx - r, cy - r, r * 2, r * 2));

                String inicial = nome.substring(0, Math.min(3, nome.length())).toUpperCase();
                g2.setColor(fgCor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inicial, cx - fm.stringWidth(inicial) / 2, cy + fm.getAscent() / 2 - 2);

                g2.setColor(sel ? MainFrame.COR_NAVY : new Color(0x333333));
                g2.setFont(new Font("Segoe UI", sel ? Font.BOLD : Font.PLAIN, 11));
                fm = g2.getFontMetrics();
                g2.drawString(nome, cx - fm.stringWidth(nome) / 2, cy + r + 18);
                g2.dispose();
            }
        };
        return card;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────────
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblModeloSel = new JLabel("Nenhum modelo selecionado");
        lblModeloSel.setFont(MainFrame.FONT_NORMAL);
        lblModeloSel.setForeground(new Color(0x888888));

        btnProximo = new JButton("Próximo: dados do veículo") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = !isEnabled() ? new Color(0xcccccc)
                         : getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(isEnabled() ? MainFrame.COR_GOLD : new Color(0x999999));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnProximo.setPreferredSize(new Dimension(240, 36));
        btnProximo.setOpaque(false);
        btnProximo.setContentAreaFilled(false);
        btnProximo.setBorderPainted(false);
        btnProximo.setFocusPainted(false);
        btnProximo.setEnabled(false);
        btnProximo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProximo.addActionListener(e -> frame.mostrarTela(
            modoNovaOS ? MainFrame.TELA_COMPOSICAO : MainFrame.TELA_VEICULO));

        rodape.add(lblModeloSel, BorderLayout.WEST);
        rodape.add(btnProximo,   BorderLayout.EAST);
        return rodape;
    }

    // ── Lógica ────────────────────────────────────────────────────────────────
    private void selecionarModelo(String nome) {
        modeloSelecionado = nome;
        lblModeloSel.setText("Modelo selecionado: " + nome);
        lblModeloSel.setForeground(MainFrame.COR_NAVY);
        btnProximo.setEnabled(true);
        repaint();
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
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
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
