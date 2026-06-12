package br.com.picarauto.view;

/**
 * Lista e cadastro de clientes — busca em tempo real, tabela e JDialog PF/PJ.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Imports do backend necessários para buscar e salvar clientes no banco
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.PessoaFisicaModel;
import br.com.picarauto.model.PessoaJuridicaModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelCadastroCliente extends JPanel {

    private final MainFrame frame;

    private JTextField    txtBusca;
    private JTable        tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    // Guarda os clientes carregados do banco na mesma ordem das linhas da tabela
    private List<ClienteModel> clientesAtuais;

    private static final String[] COLUNAS = {"Nome / Razão Social", "CPF / CNPJ", "Tipo", "Telefone", ""};

    private static final Object[][] DADOS_MOCK = {
        {"João da Silva",           "123.456.789-00",     "Pessoa Física",   "(47) 99111-2222"},
        {"Maria Oliveira",          "987.654.321-00",     "Pessoa Física",   "(47) 98222-3333"},
        {"Empresa XPTO Ltda.",      "12.345.678/0001-99", "Pessoa Jurídica", "(47) 3300-4444"},
        {"Carlos Eduardo",          "456.123.789-00",     "Pessoa Física",   "(47) 97555-6666"},
        {"Distribuidora ABC Ltda.", "98.765.432/0001-11", "Pessoa Jurídica", "(47) 3311-5555"},
    };

    public PanelCadastroCliente(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_CLIENTE), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Clientes");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        bar.add(lbl, BorderLayout.WEST);
        bar.add(criarUsuarioPanel(), BorderLayout.EAST);
        return bar;
    }

    private JPanel criarUsuarioPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 9));
        p.setOpaque(false);
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fillOval(0, 0, 30, 30);
                String car = new String(Character.toChars(0x1F697));
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(car, (30 - fm.stringWidth(car)) / 2, (30 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(30, 30));
        JLabel nome = new JLabel(MainFrame.getUsuarioLogado());
        nome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nome.setForeground(new Color(0xccddff));
        p.add(av); p.add(nome);
        return p;
    }

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JPanel criarConteudo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainFrame.COR_CREAM);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
        txtBusca = new JTextField();
        txtBusca.setPreferredSize(new Dimension(0, 36));
        txtBusca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setText("Pesquisar...");
        txtBusca.setForeground(Color.GRAY);
        txtBusca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("Pesquisar...".equals(txtBusca.getText())) {
                    txtBusca.setText(""); txtBusca.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBusca.getText().isEmpty()) {
                    txtBusca.setText("Pesquisar..."); txtBusca.setForeground(Color.GRAY);
                }
            }
        });
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
            void filtrar() {
                if (sorter == null) return;
                String txt = txtBusca.getText().trim();
                if ("Pesquisar...".equals(txt)) { sorter.setRowFilter(null); return; }
                sorter.setRowFilter(txt.isEmpty() ? null : RowFilter.regexFilter("(?i)" + txt));
            }
        });

        JButton btnNovo = criarBotaoNavy("Novo cliente", 130, 34);
        btnNovo.addActionListener(e -> abrirFormNovoCliente());

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);
        return painelBusca;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // A tabela começa vazia — carregarClientes() é chamado pelo MainFrame.mostrarTela()
        // ao navegar para TELA_CLIENTE, quando o Spring já está inicializado.
        // Chamar aqui causaria IllegalStateException pois o Spring ainda não subiu.

        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        tabela.setFont(MainFrame.FONT_NORMAL);
        tabela.setRowHeight(32);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(12, 0));
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionBackground(new Color(0xe8e3d8));
        tabela.setSelectionForeground(MainFrame.COR_NAVY);
        tabela.setFillsViewportHeight(true);
        tabela.setDefaultEditor(Object.class, null);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(MainFrame.COR_CREAM_ALT);
        header.setForeground(new Color(0x444444));
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainFrame.COR_BORDER));
        header.setReorderingAllowed(false);

        tabela.getColumnModel().getColumn(4).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(4).setCellRenderer(new EditarRenderer());

        // Detecta clique na coluna "Editar" e abre o dialog com os dados do cliente selecionado
        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewRow = tabela.rowAtPoint(e.getPoint());
                int viewCol = tabela.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol != 4) return;
                int modelRow = tabela.convertRowIndexToModel(viewRow);
                if (clientesAtuais == null || modelRow >= clientesAtuais.size()) return;
                ClienteModel selecionado = clientesAtuais.get(modelRow);
                abrirFormEdicaoCliente(selecionado);
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // Busca todos os clientes ativos no banco via ClienteController
    // e preenche a tabela. Chamado ao entrar na tela e após salvar/editar.
    public void carregarClientes() {
        modelo.setRowCount(0);
        try {
            ClienteController controller = ContextoAplicacao.getBean(ClienteController.class);
            clientesAtuais = controller.findAll();
            for (ClienteModel c : clientesAtuais) {
                String doc, tipo;
                if (c instanceof PessoaFisicaModel pf) {
                    doc  = pf.getCpf();
                    tipo = "Pessoa Física";
                } else if (c instanceof PessoaJuridicaModel pj) {
                    doc  = pj.getCnpj();
                    tipo = "Pessoa Jurídica";
                } else {
                    doc  = "-";
                    tipo = "-";
                }
                modelo.addRow(new Object[]{ c.getNomeCompleto(), doc, tipo, c.getTelefone() });
            }
        } catch (Exception ex) {
            // Exibe erro na tela caso o banco não esteja acessível
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar clientes: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Diálogo de cadastro ───────────────────────────────────────────────────
    private void abrirFormNovoCliente() {
        abrirFormCliente(null);
    }

    // Abre o dialog preenchido com os dados do cliente para edição
    private void abrirFormEdicaoCliente(ClienteModel cliente) {
        abrirFormCliente(cliente);
    }

    // Dialog unificado: null = novo cadastro, cliente preenchido = edição
    private void abrirFormCliente(ClienteModel clienteExistente) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            clienteExistente == null ? "Novo Cliente" : "Editar Cliente",
            java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 430);
        dialog.setLocationRelativeTo(this);

        boolean[] modoEmpresa = { clienteExistente instanceof PessoaJuridicaModel };

        // Campos PF
        JTextField txtNomePF  = criarCampo();
        JTextField txtCPF     = criarCampo();
        JTextField txtRG      = criarCampo();
        JTextField txtNascPF  = criarCampo();
        JTextField txtTelPF   = criarCampo();
        JTextField txtEmailPF = criarCampo();
        JTextField txtEndPF   = criarCampo();

        // Campos PJ
        JTextField txtRazao   = criarCampo();
        JTextField txtCNPJ    = criarCampo();
        JTextField txtIE      = criarCampo();
        JTextField txtTelPJ   = criarCampo();
        JTextField txtEmailPJ = criarCampo();
        JTextField txtEndPJ   = criarCampo();

        // Preenche os campos se for edição
        if (clienteExistente instanceof PessoaFisicaModel pf) {
            txtNomePF.setText(pf.getNomeCompleto());
            txtCPF.setText(pf.getCpf());
            txtRG.setText(pf.getRg());
            if (pf.getDataNascimento() != null)
                txtNascPF.setText(pf.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtTelPF.setText(pf.getTelefone());
            txtEmailPF.setText(pf.getEmail());
            txtEndPF.setText(pf.getEndereco());
        } else if (clienteExistente instanceof PessoaJuridicaModel pj) {
            txtRazao.setText(pj.getRazaoSocial());
            txtCNPJ.setText(pj.getCnpj());
            txtIE.setText(pj.getInscricaoEstadual() != null ? pj.getInscricaoEstadual() : "");
            txtTelPJ.setText(pj.getTelefone());
            txtEmailPJ.setText(pj.getEmail());
            txtEndPJ.setText(pj.getEndereco());
        }

        JPanel gridPF = new JPanel(new GridLayout(4, 2, 14, 10));
        gridPF.setOpaque(false);
        gridPF.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridPF.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        gridPF.add(criarGrupo("Nome completo",  txtNomePF));
        gridPF.add(criarGrupo("CPF",            txtCPF));
        gridPF.add(criarGrupo("RG",             txtRG));
        gridPF.add(criarGrupo("Data nasc. (dd/MM/yyyy)", txtNascPF));
        gridPF.add(criarGrupo("Telefone",       txtTelPF));
        gridPF.add(criarGrupo("E-mail",         txtEmailPF));
        gridPF.add(criarGrupo("Endereço",       txtEndPF));
        gridPF.add(new JPanel() {{ setOpaque(false); }});

        JPanel gridPJ = new JPanel(new GridLayout(4, 2, 14, 10));
        gridPJ.setOpaque(false);
        gridPJ.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridPJ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        gridPJ.add(criarGrupo("Razão Social",       txtRazao));
        gridPJ.add(criarGrupo("CNPJ",               txtCNPJ));
        gridPJ.add(criarGrupo("Inscrição Estadual", txtIE));
        gridPJ.add(criarGrupo("Telefone",           txtTelPJ));
        gridPJ.add(criarGrupo("E-mail",             txtEmailPJ));
        gridPJ.add(criarGrupo("Endereço",           txtEndPJ));
        gridPJ.add(new JPanel() {{ setOpaque(false); }});
        gridPJ.add(new JPanel() {{ setOpaque(false); }});

        JPanel camposCard = new JPanel(new CardLayout());
        camposCard.setOpaque(false);
        camposCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        camposCard.add(gridPF, "PF");
        camposCard.add(gridPJ, "PJ");

        // Exibe o painel correto se for edição
        if (clienteExistente instanceof PessoaJuridicaModel)
            ((CardLayout) camposCard.getLayout()).show(camposCard, "PJ");

        JButton btnPF = criarBotaoToggle("Pessoa Física",   modoEmpresa);
        JButton btnPJ = criarBotaoToggle("Pessoa Jurídica", modoEmpresa);

        btnPF.addActionListener(e -> {
            modoEmpresa[0] = false;
            ((CardLayout) camposCard.getLayout()).show(camposCard, "PF");
            btnPF.repaint(); btnPJ.repaint();
        });
        btnPJ.addActionListener(e -> {
            modoEmpresa[0] = true;
            ((CardLayout) camposCard.getLayout()).show(camposCard, "PJ");
            btnPF.repaint(); btnPJ.repaint();
        });

        // Desabilita o toggle de tipo ao editar — não faz sentido mudar PF para PJ
        if (clienteExistente != null) {
            btnPF.setEnabled(false);
            btnPJ.setEnabled(false);
        }

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        togglePanel.add(btnPF);
        togglePanel.add(btnPJ);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            try {
                ClienteController controller = ContextoAplicacao.getBean(ClienteController.class);

                if (!modoEmpresa[0]) {
                    // Monta ou atualiza o objeto PessoaFisicaModel com os dados do formulário
                    PessoaFisicaModel pf = (clienteExistente instanceof PessoaFisicaModel existing)
                        ? existing : new PessoaFisicaModel();
                    pf.setNomeCompleto(txtNomePF.getText().trim());
                    pf.setCpf(txtCPF.getText().replaceAll("\\D", "").trim());
                    pf.setRg(txtRG.getText().trim());
                    pf.setTelefone(txtTelPF.getText().trim());
                    pf.setEmail(txtEmailPF.getText().trim());
                    pf.setEndereco(txtEndPF.getText().trim());
                    // Converte a data digitada no formato dd/MM/yyyy para LocalDate
                    pf.setDataNascimento(LocalDate.parse(
                        txtNascPF.getText().trim(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    if (pf.getDataCadastro() == null)
                        pf.setDataCadastro(LocalDate.now());

                    // Insere novo ou atualiza existente no banco
                    if (clienteExistente == null) controller.insert(pf);
                    else controller.update(pf);

                } else {
                    // Monta ou atualiza o objeto PessoaJuridicaModel com os dados do formulário
                    PessoaJuridicaModel pj = (clienteExistente instanceof PessoaJuridicaModel existing)
                        ? existing : new PessoaJuridicaModel();
                    pj.setRazaoSocial(txtRazao.getText().trim());
                    pj.setNomeCompleto(txtRazao.getText().trim());
                    pj.setCnpj(txtCNPJ.getText().replaceAll("\\D", "").trim());
                    pj.setInscricaoEstadual(txtIE.getText().trim().isEmpty() ? null : txtIE.getText().trim());
                    pj.setTelefone(txtTelPJ.getText().trim());
                    pj.setEmail(txtEmailPJ.getText().trim());
                    pj.setEndereco(txtEndPJ.getText().trim());
                    // Converte data de abertura — usa data atual se não for edição
                    if (pj.getDataAbertura() == null)
                        pj.setDataAbertura(LocalDate.now());
                    if (pj.getDataCadastro() == null)
                        pj.setDataCadastro(LocalDate.now());

                    if (clienteExistente == null) controller.insert(pj);
                    else controller.update(pj);
                }

                dialog.dispose();
                // Recarrega a tabela para refletir o novo cadastro ou a edição
                carregarClientes();

            } catch (DateTimeParseException dtEx) {
                JOptionPane.showMessageDialog(dialog,
                    "Data inválida. Use o formato dd/MM/yyyy.",
                    "Erro de validação", JOptionPane.WARNING_MESSAGE);
            } catch (FieldValidationException | RuleValidationException valEx) {
                // Exibe a mensagem de validação do backend (campo inválido ou regra de negócio)
                JOptionPane.showMessageDialog(dialog,
                    valEx.getMessage(),
                    "Erro de validação", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(togglePanel);
        form.add(Box.createVerticalStrut(14));
        form.add(camposCard);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JButton criarBotaoToggle(String texto, boolean[] modoEmpresa) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (texto.contains("Física")   && !modoEmpresa[0])
                             || (texto.contains("Jurídica") &&  modoEmpresa[0]);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(ativo ? Color.WHITE : MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
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

    private JButton criarBotaoNavy(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
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

    static class EditarRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Editar", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}