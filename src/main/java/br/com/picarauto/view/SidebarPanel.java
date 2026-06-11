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
        btn.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_COMPOSICAO));
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

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        userPanel.setOpaque(false);
        userPanel.setMaximumSize(new Dimension(200, 52));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        avatarPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                String nome = MainFrame.getUsuarioLogado();
                if (nome == null || nome.trim().isEmpty()) nome = "Godofredo Silva";
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_GOLD);
                g2.fillOval(0, 0, 28, 28);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String init = nome.substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init,
                    (28 - fm.stringWidth(init)) / 2,
                    (28 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(28, 28));

        lblNomeUsuario = new JLabel(usuario);
        lblNomeUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNomeUsuario.setForeground(new Color(0xaabbcc));

        userPanel.add(avatarPanel);
        userPanel.add(lblNomeUsuario);
        add(userPanel);
    }
}
