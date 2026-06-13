package br.com.picarauto.view;

/**
 *
 * @author Cassiano
 */
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

    public static final Color COR_NAVY      = new Color(0x1a2744);
    public static final Color COR_NAVY_DARK = new Color(0x111d38);
    public static final Color COR_GOLD      = new Color(0xc9a86c);
    public static final Color COR_GREEN     = new Color(0x2d4a3e);
    public static final Color COR_CREAM     = new Color(0xf5f0e6);
    public static final Color COR_CREAM_ALT = new Color(0xeae5d8);
    public static final Color COR_MUTED     = new Color(0x8899bb);
    public static final Color COR_CARD_BG   = new Color(0x223060);
    public static final Color COR_BORDER    = new Color(0xd0c9b8);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font FONT_MEDIUM = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    private final CardLayout cardLayout;
    private final JPanel     painelPrincipal;

    // ── Painéis que precisam ser recarregados ao navegar ──────────────────────
    private PanelSplash               splash;
    private PanelSelecaoModelo        selecaoModelo;
    private PanelCadastroVeiculo      cadastroVeiculo;
    private PanelListaClientes        listaClientes;
    private PanelCadastroCliente      cadastroCliente;
    private PanelListaOS              listaOS;
    private PanelComposicaoOS         composicaoOS;
    private PanelCadastroColaborador  cadastroColab;
    private PanelCadastroFornecedor   cadastroFornecedor;
    private PanelCadastroPeca         cadastroPeca;
    private PanelCadastroServicos     cadastroServicos;

    // ── Seleção de marca/modelo ───────────────────────────────────────────────
    private String marcaSelecionada  = "";
    private String modeloSelecionado = "";
    private Long   idMarcaSelecionada  = null;
    private Long   idModeloSelecionado = null;

    public String getMarcaSelecionada()               { return marcaSelecionada; }
    public void   setMarcaSelecionada(String m)       { this.marcaSelecionada  = m != null ? m : ""; }
    public String getModeloSelecionado()              { return modeloSelecionado; }
    public void   setModeloSelecionado(String m)      { this.modeloSelecionado = m != null ? m : ""; }
    public Long   getIdMarcaSelecionada()             { return idMarcaSelecionada; }
    public void   setIdMarcaSelecionada(Long id)      { this.idMarcaSelecionada  = id; }
    public Long   getIdModeloSelecionado()            { return idModeloSelecionado; }
    public void   setIdModeloSelecionado(Long id)     { this.idModeloSelecionado = id; }

    private static MainFrame instancia;
    public  static MainFrame getInstance()            { return instancia; }

    private static String usuarioLogado = "";
    public  static String getUsuarioLogado()          { return usuarioLogado; }
    public  static void   setUsuarioLogado(String u)  { usuarioLogado = u; }
    
    // - Panel cadastro Cliente
    public PanelCadastroCliente getCadastroClientePanel() {
        return cadastroCliente;
    }

    // ── Constantes de tela ────────────────────────────────────────────────────
    public static final String TELA_LOGIN          = "LOGIN";
    public static final String TELA_SPLASH         = "SPLASH";
    public static final String TELA_DASHBOARD      = "DASHBOARD";
    public static final String TELA_LISTA_OS       = "LISTA_OS";
    public static final String TELA_MARCA          = "MARCA";
    public static final String TELA_MODELO         = "MODELO";
    public static final String TELA_COMPOSICAO     = "COMPOSICAO";
    public static final String TELA_LISTA_CLIENTES = "LISTA_CLIENTES";
    public static final String TELA_CLIENTE        = "CLIENTE";
    public static final String TELA_VEICULO        = "VEICULO";
    public static final String TELA_PECA           = "PECA";
    public static final String TELA_COLABORADOR    = "COLABORADOR";
    public static final String TELA_FORNECEDOR     = "FORNECEDOR";
    public static final String TELA_SERVICOS       = "SERVICOS";
    public static final String TELA_MARCAS_MOD     = "MARCAS_MOD";

    public MainFrame() {
        instancia = this;
        setTitle("AV CAR AUTO CENTER — Sistema de Controle de Oficina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout      = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // ── Instancia todos os painéis ────────────────────────────────────────
        PanelLogin        login        = new PanelLogin(this);
        splash                         = new PanelSplash(this);
        PanelDashboard    dashboard    = new PanelDashboard(this);
        PanelSelecaoMarca selecaoMarca = new PanelSelecaoMarca(this);
        selecaoModelo                  = new PanelSelecaoModelo(this);
        listaOS                        = new PanelListaOS(this);
        composicaoOS                   = new PanelComposicaoOS(this);
        listaClientes                  = new PanelListaClientes(this);
        cadastroCliente                = new PanelCadastroCliente(this);
        cadastroVeiculo                = new PanelCadastroVeiculo(this);
        cadastroColab                  = new PanelCadastroColaborador(this);
        cadastroFornecedor             = new PanelCadastroFornecedor(this);
        cadastroPeca                   = new PanelCadastroPeca(this);
        cadastroServicos               = new PanelCadastroServicos(this);
        PanelMarcasModelos marcasMod   = new PanelMarcasModelos(this);

        // ── Registra no CardLayout ────────────────────────────────────────────
        painelPrincipal.add(login,             TELA_LOGIN);
        painelPrincipal.add(splash,            TELA_SPLASH);
        painelPrincipal.add(dashboard,         TELA_DASHBOARD);
        painelPrincipal.add(selecaoMarca,      TELA_MARCA);
        painelPrincipal.add(selecaoModelo,     TELA_MODELO);
        painelPrincipal.add(listaOS,           TELA_LISTA_OS);
        painelPrincipal.add(composicaoOS,      TELA_COMPOSICAO);
        painelPrincipal.add(listaClientes,     TELA_LISTA_CLIENTES);
        painelPrincipal.add(cadastroCliente,   TELA_CLIENTE);
        painelPrincipal.add(cadastroVeiculo,   TELA_VEICULO);
        painelPrincipal.add(cadastroPeca,      TELA_PECA);
        painelPrincipal.add(cadastroColab,     TELA_COLABORADOR);
        painelPrincipal.add(cadastroFornecedor,TELA_FORNECEDOR);
        painelPrincipal.add(cadastroServicos,  TELA_SERVICOS);
        painelPrincipal.add(marcasMod,         TELA_MARCAS_MOD);

        add(painelPrincipal);
        mostrarTela(TELA_SPLASH);
    }

    public void mostrarTela(String nomeTela) {
        if (TELA_SPLASH.equals(nomeTela) && splash != null)
            splash.reiniciar();

        // Todas as chamadas ao banco só ocorrem após o Spring estar inicializado.
        // Durante a construção dos painéis (antes do Spring subir), isReady() é false
        // e nenhuma chamada ao banco é feita, evitando IllegalStateException.
        if (br.com.picarauto.util.ContextoAplicacao.isReady()) {

            if (TELA_MODELO.equals(nomeTela))
                selecaoModelo.carregarModelos();

            if (TELA_VEICULO.equals(nomeTela))
                cadastroVeiculo.preencherSelecoes();

            if (TELA_LISTA_OS.equals(nomeTela))
                listaOS.carregarOS();

            if (TELA_COMPOSICAO.equals(nomeTela))
                composicaoOS.carregarDados();

            if (TELA_LISTA_CLIENTES.equals(nomeTela))
                listaClientes.carregarClientes();

            if (TELA_CLIENTE.equals(nomeTela))
                cadastroCliente.carregarClientes();

            if (TELA_COLABORADOR.equals(nomeTela))
                cadastroColab.carregarColaboradores();

            if (TELA_FORNECEDOR.equals(nomeTela))
                cadastroFornecedor.carregarFornecedores();

            if (TELA_PECA.equals(nomeTela))
                cadastroPeca.carregarPecas();

            if (TELA_SERVICOS.equals(nomeTela))
                cadastroServicos.carregarServicos();
        }

        cardLayout.show(painelPrincipal, nomeTela);
        SidebarPanel.atualizarTodas();
    }

    public void adicionarTela(JPanel painel, String nomeTela) {
        painelPrincipal.add(painel, nomeTela);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}