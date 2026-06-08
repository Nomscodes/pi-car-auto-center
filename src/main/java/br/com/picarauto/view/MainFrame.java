package br.com.picarauto.view;

/**
 *
 * @author Cassiano
 */
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final Color COR_NAVY      = new Color(0x1a2744);
    public static final Color COR_NAVY_DARK = new Color(0x111d38);
    public static final Color COR_GOLD      = new Color(0xc9a86c);
    public static final Color COR_GREEN     = new Color(0x2d4a3e);
    public static final Color COR_CREAM     = new Color(0xf5f0e6);
    public static final Color COR_CREAM_ALT = new Color(0xeae5d8);
    public static final Color COR_MUTED     = new Color(0x8899bb);
    public static final Color COR_CARD_BG   = new Color(0x223060);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,   18);
    public static final Font FONT_MEDIUM = new Font("Segoe UI", Font.BOLD,   13);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN,  12);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN,  11);

    private final CardLayout cardLayout;
    private final JPanel     painelPrincipal;
    private PanelSelecaoModelo selecaoModelo;

    public static final String TELA_SPLASH     = "SPLASH";
    public static final String TELA_DASHBOARD  = "DASHBOARD";
    public static final String TELA_LISTA_OS   = "LISTA_OS";
    public static final String TELA_MARCA      = "MARCA";
    public static final String TELA_MODELO     = "MODELO";
    public static final String TELA_COMPOSICAO = "COMPOSICAO";
    public static final String TELA_CLIENTE    = "CLIENTE";
    public static final String TELA_VEICULO    = "VEICULO";
    public static final String TELA_PECA          = "PECA";
    public static final String TELA_COLABORADOR = "COLABORADOR";

    public MainFrame() {
        setTitle("AV CAR AUTO CENTER — Sistema de Controle de Oficina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout      = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        PanelSplash           splash           = new PanelSplash(this);
        PanelDashboard        dashboard        = new PanelDashboard(this);
        PanelSelecaoMarca     selecaoMarca     = new PanelSelecaoMarca(this);
        selecaoModelo    = new PanelSelecaoModelo(this);
        PanelListaOS          listaOS          = new PanelListaOS(this);
        PanelComposicaoOS     composicaoOS     = new PanelComposicaoOS(this);
        PanelCadastroCliente  cadastroCliente  = new PanelCadastroCliente(this);
        PanelCadastroVeiculo  cadastroVeiculo  = new PanelCadastroVeiculo(this);
        PanelCadastroPeca          cadastroPeca       = new PanelCadastroPeca(this);
        PanelCadastroColaborador  cadastroColab      = new PanelCadastroColaborador(this);

        painelPrincipal.add(splash,          TELA_SPLASH);
        painelPrincipal.add(dashboard,       TELA_DASHBOARD);
        painelPrincipal.add(selecaoMarca,    TELA_MARCA);
        painelPrincipal.add(selecaoModelo,   TELA_MODELO);
        painelPrincipal.add(listaOS,         TELA_LISTA_OS);
        painelPrincipal.add(composicaoOS,    TELA_COMPOSICAO);
        painelPrincipal.add(cadastroCliente, TELA_CLIENTE);
        painelPrincipal.add(cadastroVeiculo, TELA_VEICULO);
        painelPrincipal.add(cadastroPeca,  TELA_PECA);
        painelPrincipal.add(cadastroColab, TELA_COLABORADOR);

        add(painelPrincipal);
        mostrarTela(TELA_SPLASH);
    }

    public void mostrarTela(String nomeTela) {
        if (TELA_MODELO.equals(nomeTela)) {
            selecaoModelo.carregarModelos();
        }
        cardLayout.show(painelPrincipal, nomeTela);
    }

    public void adicionarTela(JPanel painel, String nomeTela) {
        painelPrincipal.add(painel, nomeTela);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
