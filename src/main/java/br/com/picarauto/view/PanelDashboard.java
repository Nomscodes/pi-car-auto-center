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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PanelDashboard extends JPanel {

    private final MainFrame frame;

    private static final String[][] CARDS = {
        {"Ordens de serviço",  "12 abertas",        MainFrame.TELA_LISTA_OS},
        {"Clientes",           "Pessoa física / PJ", MainFrame.TELA_LISTA_CLIENTES},
        {"Veículos",           "Por marca",          MainFrame.TELA_MARCA},
        {"Peças",              "234 itens",          MainFrame.TELA_PECA},
        {"Colaboradores",      "8 ativos",           MainFrame.TELA_COLABORADOR},
        {"Fornecedores",       "15 cadastrados",     MainFrame.TELA_FORNECEDOR},
        {"Serviços",           "Interno e externo",  MainFrame.TELA_SERVICOS},
        {"Marcas e modelos",   "22 marcas",          MainFrame.TELA_MARCAS_MOD},
    };

    public PanelDashboard(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_NAVY);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopBar(), BorderLayout.NORTH);
        add(criarGradeCards(), BorderLayout.CENTER);
    }

    private JPanel criarTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(MainFrame.COR_NAVY_DARK);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel esquerda = new JPanel();
        esquerda.setOpaque(false);
        esquerda.setLayout(new BoxLayout(esquerda, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel("AV CAR AUTO CENTER");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNome.setForeground(MainFrame.COR_GOLD);

        JLabel lblSub = new JLabel("Painel principal");
        lblSub.setFont(MainFrame.FONT_SMALL);
        lblSub.setForeground(MainFrame.COR_MUTED);

        esquerda.add(lblNome);
        esquerda.add(lblSub);

        // Direita: data + usuario + botao sair
        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        direita.setOpaque(false);

        String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel lblData = new JLabel(dataHoje);
        lblData.setFont(MainFrame.FONT_SMALL);
        lblData.setForeground(MainFrame.COR_MUTED);

        JPanel separador = new JPanel();
        separador.setOpaque(false);
        separador.setPreferredSize(new Dimension(1, 20));
        separador.setBackground(MainFrame.COR_MUTED);

        JLabel lblUsuario = new JLabel("👤 " + MainFrame.getUsuarioLogado());
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(MainFrame.COR_CREAM);

        JButton btnSair = new JButton("Sair") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x8B1A1A));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btnSair.setPreferredSize(new Dimension(60, 26));
        btnSair.setBorderPainted(false);
        btnSair.setContentAreaFilled(false);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(frame, "Deseja sair do sistema?", "Sair", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) frame.mostrarTela(MainFrame.TELA_LOGIN);
        });

        direita.add(lblData);
        direita.add(lblUsuario);
        direita.add(btnSair);

        topBar.add(esquerda, BorderLayout.WEST);
        topBar.add(direita,  BorderLayout.EAST);
        return topBar;
    }

    private JPanel criarGradeCards() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel grade = new JPanel(new GridLayout(2, 4, 14, 14));
        grade.setOpaque(false);

        for (String[] info : CARDS) {
            grade.add(criarCard(info[0], info[1], info[2]));
        }

        wrapper.add(grade, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel criarCard(String titulo, String subtitulo, String telaDest) {
        JPanel card = new JPanel() {
            private boolean hover = false;
            {
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setLayout(new GridBagLayout());
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                    @Override public void mouseExited (MouseEvent e) { hover = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        if (!telaDest.isEmpty()) frame.mostrarTela(telaDest);
                    }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg    = hover ? new Color(0x2a3870) : MainFrame.COR_CARD_BG;
                Color borda = hover ? MainFrame.COR_GOLD  : new Color(0x2a3870);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(borda);
                g2.setStroke(new BasicStroke(hover ? 1.5f : 0.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(MainFrame.FONT_MEDIUM);
        lblTitulo.setForeground(MainFrame.COR_CREAM);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(MainFrame.FONT_SMALL);
        lblSub.setForeground(MainFrame.COR_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(lblTitulo);
        inner.add(Box.createVerticalStrut(6));
        inner.add(lblSub);
        card.add(inner);
        return card;
    }
}
