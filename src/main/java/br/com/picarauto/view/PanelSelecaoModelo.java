package br.com.picarauto.view;

/**
 * Seleção de modelo do veículo — grid 4 colunas, mesma estética da tela de marcas.
 * Toda a lógica de carregarModelos(), setMarcaSelecionada() e modoNovaOS preservada.
 *
 * @author Cassiano
 */
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

// Imports do backend para carregar modelos do banco e salvar o id do modelo selecionado
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.model.ModeloModel;

public class PanelSelecaoModelo extends JPanel {

    private final MainFrame frame;

    private static String  marcaAtual  = "";
    private static boolean modoNovaOS  = false;

    private String  modeloSelecionado  = null;
    private JButton btnProximo;
    private JLabel  lblModeloSel;
    private JLabel  lblTitulo;
    private JPanel  gradeModelos;

    // Guarda os modelos carregados do banco para identificar o id ao selecionar
    private List<ModeloModel> modelosAtuais = null;

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

    private static final Map<String, String> ARQUIVO_OVERRIDE = new HashMap<>();
    static {
        ARQUIVO_OVERRIDE.put("hilux",        "hillux");
        ARQUIVO_OVERRIDE.put("broncosport",  "bronco");
        ARQUIVO_OVERRIDE.put("nivus",        "nirvus");
        ARQUIVO_OVERRIDE.put("t-cross",      "t-cross");
        ARQUIVO_OVERRIDE.put("onixplus",     "onixplus");
        ARQUIVO_OVERRIDE.put("corollacross", "corollacross");
        ARQUIVO_OVERRIDE.put("toro",         "toro");
        ARQUIVO_OVERRIDE.put("pulse",        "pulse");
        ARQUIVO_OVERRIDE.put("argo",         "argo");
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

        // BOTÃO TELA SELEÇÃO MODELO DE VOLTAR PARA SELEÇÃO DE MARCA
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
        modelosAtuais     = null;
        if (lblModeloSel != null) {
            lblModeloSel.setText("Nenhum modelo selecionado");
            lblModeloSel.setForeground(new Color(0x888888));
        }
        if (btnProximo != null) btnProximo.setEnabled(false);
        if (gradeModelos == null) return;

        gradeModelos.removeAll();
        frame.setModeloSelecionado("");
        frame.setIdModeloSelecionado(null);

        Color[] corMarca = getMarcaCor(marcaAtual);

        // Tenta carregar os modelos do banco usando o id da marca salvo no MainFrame.
        // Se o id não estiver disponível ou o banco falhar, usa o Map estático como fallback.
        Long idMarca = frame.getIdMarcaSelecionada();
        if (idMarca != null) {
            try {
                ModeloController modeloController = ContextoAplicacao.getBean(ModeloController.class);
                modelosAtuais = modeloController.findAllByIdMarca(idMarca);
                for (ModeloModel m : modelosAtuais)
                    gradeModelos.add(criarCardModelo(m.getNomeModelo(), corMarca[0], corMarca[1]));
            } catch (Exception ex) {
                // Banco indisponível — cai no fallback estático
                modelosAtuais = null;
                carregarModelosFallback(corMarca);
            }
        } else {
            // id da marca não disponível — usa o Map estático
            carregarModelosFallback(corMarca);
        }

        if ("Jeep".equals(marcaAtual) || "Mitsubishi".equals(marcaAtual)) {
            int total = gradeModelos.getComponentCount();
            int faltam = Math.max(0, 8 - total);
            for (int i = 0; i < faltam; i++)
                gradeModelos.add(criarCardVazio());
        }

        gradeModelos.revalidate();
        gradeModelos.repaint();
    }

    // Preenche a grade com os nomes do Map estático (usado como fallback quando o banco não está disponível)
    private void carregarModelosFallback(Color[] corMarca) {
        String[] modelos = MODELOS_POR_MARCA.getOrDefault(marcaAtual, new String[]{});
        for (String nome : modelos)
            gradeModelos.add(criarCardModelo(nome, corMarca[0], corMarca[1]));
    }

    private Color[] getMarcaCor(String marca) {
        for (Object[] m : PanelSelecaoMarca.MARCAS)
            if (m[3].equals(marca)) return new Color[]{(Color) m[1], (Color) m[2]};
        return new Color[]{MainFrame.COR_NAVY, Color.WHITE};
    }

    private JPanel criarCardModelo(String nome, Color bgCor, Color fgCor) {
        final ImageIcon fotoIcon = carregarFotoModelo(nome);

        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        selecionarModelo(nome);
                        if (e.getClickCount() == 2) navegarParaVeiculo();
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = nome.equals(modeloSelecionado);
                g2.setColor(sel ? new Color(255, 250, 240) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                Color bdrColor = sel   ? MainFrame.COR_GOLD
                               : hover ? new Color(0xc9a86c)
                                       : new Color(0xd0c9b8);
                g2.setColor(bdrColor);
                g2.setStroke(new BasicStroke(sel ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setPreferredSize(new Dimension(140, 110));

        JLabel lblFoto;
        if (fotoIcon != null) {
            lblFoto = new JLabel(fotoIcon, SwingConstants.CENTER);
        } else {
            lblFoto = new JLabel() {
                @Override public Dimension getPreferredSize() { return new Dimension(110, 70); }
                @Override public Dimension getMinimumSize()   { return getPreferredSize(); }
                @Override public Dimension getMaximumSize()   { return getPreferredSize(); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    g2.setColor(bgCor);
                    g2.fillOval(cx - 26, cy - 26, 52, 52);
                    g2.setColor(fgCor);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    String ini = nome.substring(0, Math.min(3, nome.length())).toUpperCase();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(ini, cx - fm.stringWidth(ini) / 2, cy + fm.getAscent() / 2 - 1);
                    g2.dispose();
                }
            };
        }
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nome, SwingConstants.CENTER);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNome.setForeground(new Color(0x444444));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblFoto);
        card.add(Box.createVerticalStrut(5));
        card.add(lblNome);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel criarCardVazio() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0xd0c9b8));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(140, 110));
        return card;
    }

    private ImageIcon carregarFotoModelo(String nome) {
        String chave = nome.toLowerCase().replace(" ", "").replace("/", "-");
        String fileBase = ARQUIVO_OVERRIDE.getOrDefault(chave, chave);
        java.net.URL url = getClass().getResource("/images/" + fileBase + ".png");
        if (url != null) {
            try {
                BufferedImage bi = ImageIO.read(url);
                if (bi != null) {
                    Image scaled = bi.getScaledInstance(110, 70, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            } catch (java.io.IOException ignored) {}
        }
        return null;
    }

    // ── Rodapé ────────────────────────────────────────────────────────────────
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(new EmptyBorder(10, 20, 10, 20));

        lblModeloSel = new JLabel("Nenhum modelo selecionado");
        lblModeloSel.setFont(MainFrame.FONT_NORMAL);
        lblModeloSel.setForeground(new Color(0x888888));

        // BOTÃO TELA SELEÇÃO MODELO DE AVANÇAR PARA CADASTRO DE VEÍCULO
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
        btnProximo.addActionListener(e -> navegarParaVeiculo());

        rodape.add(lblModeloSel, BorderLayout.WEST);
        rodape.add(btnProximo,   BorderLayout.EAST);
        return rodape;
    }

    // ── Lógica ────────────────────────────────────────────────────────────────
    private void navegarParaVeiculo() {
        if (modeloSelecionado == null) return;
        frame.mostrarTela(modoNovaOS ? MainFrame.TELA_COMPOSICAO : MainFrame.TELA_VEICULO);
    }

    private void selecionarModelo(String nome) {
        modeloSelecionado = nome;
        frame.setModeloSelecionado(nome);
        lblModeloSel.setText("Modelo selecionado: " + nome);
        lblModeloSel.setForeground(MainFrame.COR_NAVY);
        btnProximo.setEnabled(true);

        // Se os modelos foram carregados do banco, busca o id do modelo selecionado
        // e armazena no MainFrame para uso no cadastro de veículo
        if (modelosAtuais != null) {
            for (ModeloModel m : modelosAtuais) {
                if (m.getNomeModelo().equalsIgnoreCase(nome)) {
                    frame.setIdModeloSelecionado(m.getId());
                    break;
                }
            }
        }

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