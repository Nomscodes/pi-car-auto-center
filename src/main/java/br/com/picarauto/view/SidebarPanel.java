package br.com.picarauto.view;

/**
 * Sidebar de navegação reutilizável — presente em todas as telas internas.
 * Largura fixa 200px, fundo navy. Botão "Nova OS" no topo (gold).
 * Ícones em Segoe UI Symbol; username dinâmico com avatar no rodapé.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    private final MainFrame frame;
    private final String    telaAtiva;

    private JLabel lblNomeUsuario;
    private JPanel avatarPanel;

    private static final List<SidebarPanel> INSTANCIAS = new ArrayList<>();

    private static final Object[][] ITENS = {
        {"⊞", "Dashboard",       MainFrame.TELA_DASHBOARD,      "PRINCIPAL"},
        {"≡", "Ordens de Serv.", MainFrame.TELA_LISTA_OS,       "PRINCIPAL"},
        {"♟", "Clientes",        MainFrame.TELA_LISTA_CLIENTES, "CADASTROS"},
        {"▣", "Veículos",   MainFrame.TELA_MARCA,          "CADASTROS"},
        {"⚙", "Peças",      MainFrame.TELA_PECA,           "CADASTROS"},
        {"◈", "Colaboradores",   MainFrame.TELA_COLABORADOR,    "CADASTROS"},
        {"◫", "Fornecedores",    MainFrame.TELA_FORNECEDOR,     "CADASTROS"},
        {"◆", "Marcas/Modelos",  MainFrame.TELA_MARCAS_MOD,     "CONFIG"},
        {"◧", "Serviços",   MainFrame.TELA_SERVICOS,       "CONFIG"},
    };

    public SidebarPanel(MainFrame frame, String telaAtiva) {
        this.frame     = frame;
        this.telaAtiva = telaAtiva;
        setBackground(MainFrame.COR_NAVY);
        setPreferredSize(new Dimension(200, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        construirUI();
        INSTANCIAS.add(this);
    }

    public static void atualizarTodas() {
        for (SidebarPanel s : INSTANCIAS) s.atualizarUsuario();
    }

    public void atualizarUsuario() {
        String usuario = MainFrame.getUsuarioLogado();
        if (usuario == null || usuario.trim().isEmpty()) usuario = "Godofredo Silva";
        if (lblNomeUsuario != null) lblNomeUsuario.setText(usuario);
        if (avatarPanel   != null) avatarPanel.repaint();
    }

    public void setItemAtivo(String tela) {}

    private void construirUI() {
        add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel wrapOS = new JPanel(new BorderLayout());
        wrapOS.setOpaque(false);
        wrapOS.setMaximumSize(new Dimension(200, 56));
        wrapOS.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapOS.setBorder(new EmptyBorder(0, 12, 12, 12));
        wrapOS.add(criarBotaoNovaOS(), BorderLayout.CENTER);
        add(wrapOS);

        String secaoAtual = "";
        for (Object[] item : ITENS) {
            String secao = (String) item[3];
            if (!secao.equals(secaoAtual)) {
                if (!secaoAtual.isEmpty()) add(Box.createRigidArea(new Dimension(0, 4)));
                adicionarLabelSecao(secao);
                secaoAtual = secao;
            }
            adicionarItem((String) item[0], (String) item[1], (String) item[2]);
        }

        add(Box.createVerticalGlue());
        adicionarDivisor();
        adicionarUsuario();
    }

    private JButton criarBotaoNovaOS() {
        JButton btn = new JButton("+ Nova OS") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(176, 40));
        btn.addActionListener(e -> abrirDialogNovaOS());
        return btn;
    }

    private void adicionarLabelSecao(String titulo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lbl.setForeground(new Color(0x4a5a7a));
        lbl.setBorder(new EmptyBorder(8, 16, 4, 16));
        lbl.setMaximumSize(new Dimension(200, 26));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lbl);
    }

    private void adicionarItem(String icone, String label, String tela) {
        boolean ativo = tela.equals(telaAtiva);
        Color   cor   = ativo ? MainFrame.COR_GOLD : MainFrame.COR_MUTED;

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            private boolean hover = false;
            {
                setOpaque(false);
                setMaximumSize(new Dimension(200, 38));
                setPreferredSize(new Dimension(200, 38));
                setAlignmentX(Component.LEFT_ALIGNMENT);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { frame.mostrarTela(tela); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (ativo) {
                    g2.setColor(new Color(201, 168, 108, 30));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(MainFrame.COR_GOLD);
                    g2.fillRect(getWidth() - 3, 0, 3, getHeight());
                } else if (hover) {
                    g2.setColor(new Color(255, 255, 255, 15));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        item.setBorder(new EmptyBorder(7, 16, 7, 4));

        JLabel lblIcon = new JLabel(icone + " ");
        lblIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
        lblIcon.setForeground(cor);

        JLabel lblText = new JLabel(label);
        lblText.setFont(new Font("Segoe UI", ativo ? Font.BOLD : Font.PLAIN, 12));
        lblText.setForeground(cor);

        item.add(lblIcon);
        item.add(lblText);
        add(item);
    }

    private void adicionarDivisor() {
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0x2a3a5a));
                g.fillRect(12, 0, getWidth() - 24, 1);
            }
        };
        d.setOpaque(false);
        d.setMaximumSize(new Dimension(200, 1));
        d.setPreferredSize(new Dimension(200, 1));
        d.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(d);
    }

    private void adicionarUsuario() {
        String usuario = MainFrame.getUsuarioLogado();
        if (usuario == null || usuario.trim().isEmpty()) usuario = "Godofredo Silva";

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 8));
        userPanel.setOpaque(false);
        userPanel.setMaximumSize(new Dimension(200, 52));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        avatarPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_GOLD);
                g2.fillOval(0, 0, 28, 28);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String ini = MainFrame.getUsuarioLogado() != null
                    ? MainFrame.getUsuarioLogado().substring(0, 1).toUpperCase() : "G";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(ini,
                    (28 - fm.stringWidth(ini)) / 2,
                    (28 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(28, 28));

        lblNomeUsuario = new JLabel(usuario);
        lblNomeUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNomeUsuario.setForeground(new Color(0xaabbcc));

        JButton btnDeslogar = criarMiniBotao("↩", "Deslogar");
        btnDeslogar.addActionListener(e -> {
            frame.mostrarTela(MainFrame.TELA_SPLASH);
            Timer t = new Timer(2000, ev -> frame.mostrarTela(MainFrame.TELA_LOGIN));
            t.setRepeats(false);
            t.start();
        });

        JButton btnSair = criarMiniBotao("✕", "Sair");
        btnSair.addActionListener(e -> {
            frame.mostrarTela(MainFrame.TELA_SPLASH);
            Timer t = new Timer(2000, ev -> System.exit(0));
            t.setRepeats(false);
            t.start();
        });

        userPanel.add(avatarPanel);
        userPanel.add(lblNomeUsuario);
        userPanel.add(btnDeslogar);
        userPanel.add(btnSair);
        add(userPanel);
    }

    // ── Mini botões Deslogar / Sair ───────────────────────────────────────────
    private JButton criarMiniBotao(String icone, String tooltip) {
        JButton btn = new JButton(icone) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x2a3a5a) : new Color(0x111d38));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(icone,
                    (getWidth()  - fm.stringWidth(icone)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(24, 24));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        return btn;
    }

    // ── Dialog Nova OS ────────────────────────────────────────────────────────
    private void abrirDialogNovaOS() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Nova Ordem de Serviço", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 340);
        dialog.setLocationRelativeTo(this);

        String[] clientes      = {"Selecione...", "Marcos Silva", "Ana Pereira",
                                   "Roberto Leal", "Carla Moura", "Fábio Nunes", "Juliana Costa"};
        String[] colaboradores = {"Selecione...", "João Mecânico", "Pedro Eletricista",
                                   "Carlos Funileiro", "Maria Pintora", "Luiz Borracheiro"};
        String[] marcas        = {"Selecione...", "Chevrolet", "Volkswagen", "Hyundai",
                                   "Toyota", "Ford", "Fiat", "Honda"};
        String[][] modelos     = {
            {},
            {"Onix", "Tracker", "Cruze", "S10", "Spin"},
            {"Gol", "Polo", "T-Cross", "Virtus", "Nivus"},
            {"HB20", "Creta", "Tucson", "Santa Fe"},
            {"Corolla", "Hilux", "SW4", "Yaris"},
            {"Ka", "EcoSport", "Ranger", "Territory"},
            {"Argo", "Pulse", "Cronos", "Toro"},
            {"Civic", "HR-V", "City", "Fit"},
        };

        JComboBox<String> cmbCliente = criarCombo(clientes);
        JComboBox<String> cmbColab   = criarCombo(colaboradores);
        JComboBox<String> cmbMarca   = criarCombo(marcas);
        JComboBox<String> cmbModelo  = criarCombo(new String[]{"Selecione primeiro a marca..."});
        cmbModelo.setEnabled(false);
        JTextField txtPlaca = criarCampo();

        cmbMarca.addActionListener(e -> {
            int idx = cmbMarca.getSelectedIndex();
            cmbModelo.removeAllItems();
            if (idx > 0 && idx < modelos.length) {
                cmbModelo.setEnabled(true);
                cmbModelo.addItem("Selecione o modelo...");
                for (String m : modelos[idx]) cmbModelo.addItem(m);
            } else {
                cmbModelo.setEnabled(false);
                cmbModelo.addItem("Selecione primeiro a marca...");
            }
        });

        JPanel row1 = criarLinhaGrid(2);
        row1.add(criarGrupoCombo("Cliente",      cmbCliente));
        row1.add(criarGrupoCombo("Colaborador",  cmbColab));

        JPanel row2 = criarLinhaGrid(2);
        row2.add(criarGrupoCombo("Marca",  cmbMarca));
        row2.add(criarGrupoCombo("Modelo", cmbModelo));

        JPanel row3 = criarLinhaGrid(2);
        row3.add(criarGrupoCampo("Placa", txtPlaca));
        row3.add(new JPanel() {{ setOpaque(false); }});

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar OS", 110, 34);
        btnSalv.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "OS cadastrada com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(row1);
        form.add(Box.createVerticalStrut(10));
        form.add(row2);
        form.add(Box.createVerticalStrut(10));
        form.add(row3);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    // ── Helpers do dialog ─────────────────────────────────────────────────────
    private JPanel criarLinhaGrid(int cols) {
        JPanel row = new JPanel(new GridLayout(1, cols, 14, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        return row;
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

    private JPanel criarGrupoCampo(String label, JTextField campo) {
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
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
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

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
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
}
