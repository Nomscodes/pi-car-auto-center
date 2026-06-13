package br.com.picarauto.view;

/**
 * Lista e cadastro de colaboradores — integrado ao backend via ColaboradorController.
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// Imports do backend
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.ColaboradorController;
import br.com.picarauto.model.ColaboradorModel;
import br.com.picarauto.model.FuncaoColaboradorModel;
import br.com.picarauto.repository.IFuncaoColaboradorRepository;

public class PanelCadastroColaborador extends JPanel {

    private final MainFrame frame;

    private JTextField    txtBusca;
    private JTable        tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;

    // Mantém a lista carregada do banco na mesma ordem das linhas da tabela
    private List<ColaboradorModel> colaboradoresAtuais;

    private static final String[] COLUNAS = {"Nome", "CPF", "Função", "Telefone", ""};

    private static final String[] FUNCOES = {
        "Mecânico", "Eletricista", "Funileiro", "Pintor",
        "Borracheiro", "Auxiliar", "Lavador", "Recepcionista"
    };

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PanelCadastroColaborador(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_COLABORADOR), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Colaboradores");
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
            @Override public void insertUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        JButton btnNovo = criarBotaoNavy("Novo colaborador", 150, 34);
        btnNovo.addActionListener(e -> abrirFormColaborador(null));

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);

        String[] opcoes = {"Padrão", "A-Z (Nome)", "Z-A (Nome)",
            "Função: Mecânico", "Função: Eletricista", "Função: Funileiro", "Função: Pintor",
            "Função: Borracheiro", "Função: Auxiliar", "Função: Lavador", "Função: Recepcionista"};
        cmbOrdenar = new JComboBox<>(opcoes);
        cmbOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbOrdenar.setBackground(Color.WHITE);
        cmbOrdenar.setPreferredSize(new Dimension(200, 32));
        cmbOrdenar.addActionListener(e -> aplicarFiltros());

        JPanel filtroRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        filtroRow.setOpaque(false);
        filtroRow.add(cmbOrdenar);

        JPanel barra = new JPanel(new BorderLayout(0, 6));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        barra.add(painelBusca, BorderLayout.NORTH);
        barra.add(filtroRow, BorderLayout.SOUTH);
        return barra;
    }

    private void aplicarFiltros() {
        if (sorter == null) return;
        String sel = cmbOrdenar == null ? "Padrão" : (String) cmbOrdenar.getSelectedItem();
        if (sel == null) sel = "Padrão";
        String txt = txtBusca.getText().trim();
        boolean hasText = !txt.isEmpty() && !"Pesquisar...".equals(txt);

        sorter.setSortKeys(java.util.Collections.emptyList());
        if ("A-Z (Nome)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Z-A (Nome)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        java.util.List<RowFilter<Object, Object>> filtros = new java.util.ArrayList<>();
        if (hasText) filtros.add(RowFilter.regexFilter("(?i)" + txt));
        if (sel.startsWith("Função: ")) {
            String func = java.util.regex.Pattern.quote(sel.substring(8));
            filtros.add(RowFilter.regexFilter("(?i)^" + func + "$", 2));
        }
        sorter.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

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

        // Clique na coluna "Editar" abre o form de edição
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (col == 4 && row >= 0 && colaboradoresAtuais != null) {
                    int modelRow = tabela.convertRowIndexToModel(row);
                    if (modelRow < colaboradoresAtuais.size()) {
                        abrirFormColaborador(colaboradoresAtuais.get(modelRow));
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // ── Carrega do banco ───────────────────────────────────────────────────────
    public void carregarColaboradores() {
        modelo.setRowCount(0);
        try {
            ColaboradorController controller = ContextoAplicacao.getBean(ColaboradorController.class);
            colaboradoresAtuais = controller.findAll();

            for (ColaboradorModel c : colaboradoresAtuais) {
                String funcao = c.getFuncao() != null ? c.getFuncao().getFuncao() : "";
                modelo.addRow(new Object[]{
                    c.getNomeCompleto(),
                    c.getCpf(),
                    funcao,
                    c.getTelefone(),
                    ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar colaboradores: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Diálogo de cadastro / edição ──────────────────────────────────────────
    private void abrirFormColaborador(ColaboradorModel colaboradorExistente) {
        boolean editando = colaboradorExistente != null;
        String titulo = editando ? "Editar Colaborador" : "Novo Colaborador";

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            titulo, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(480, 560);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTextField txtNome     = criarCampo();
        JTextField txtCPF      = criarCampo();
        JTextField txtAdmissao = criarCampo();
        JTextField txtTelefone = criarCampo();
        JTextField txtEmail    = criarCampo();
        JTextField txtEndereco = criarCampo();
        JTextField txtSalario  = criarCampo();

        // Preenche os campos se estiver editando
        if (editando) {
            txtNome.setText(colaboradorExistente.getNomeCompleto());
            txtCPF.setText(colaboradorExistente.getCpf());
            txtCPF.setEditable(false); // CPF não pode mudar
            txtCPF.setBackground(new Color(0xEEEEEE));
            if (colaboradorExistente.getDataAdmissao() != null)
                txtAdmissao.setText(colaboradorExistente.getDataAdmissao().format(FMT_DATA));
            txtTelefone.setText(colaboradorExistente.getTelefone());
            txtEmail.setText(colaboradorExistente.getEmail());
            txtEndereco.setText(colaboradorExistente.getEndereco());
            txtSalario.setText(String.valueOf(colaboradorExistente.getSalario()));
        }

        JPanel grid = new JPanel(new GridLayout(4, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        grid.add(criarGrupo("Nome completo *", txtNome));
        grid.add(criarGrupo("CPF (só números) *", txtCPF));
        grid.add(criarGrupo("Data admissão (dd/mm/aaaa) *", txtAdmissao));
        grid.add(criarGrupo("Telefone *", txtTelefone));
        grid.add(criarGrupo("E-mail *", txtEmail));
        grid.add(criarGrupo("Endereço *", txtEndereco));
        grid.add(criarGrupo("Salário *", txtSalario));

        // JRadioButton 2×4 para Função
        JPanel funcaoGrid = new JPanel(new GridLayout(4, 2, 8, 6));
        funcaoGrid.setBackground(MainFrame.COR_CREAM);
        funcaoGrid.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1), "Função *",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 11), MainFrame.COR_NAVY));

        ButtonGroup grupo = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[FUNCOES.length];
        String funcaoAtual = editando && colaboradorExistente.getFuncao() != null
            ? colaboradorExistente.getFuncao().getFuncao() : FUNCOES[0];

        for (int i = 0; i < FUNCOES.length; i++) {
            radios[i] = new JRadioButton(FUNCOES[i]);
            radios[i].setFont(MainFrame.FONT_NORMAL);
            radios[i].setForeground(MainFrame.COR_NAVY);
            radios[i].setBackground(MainFrame.COR_CREAM);
            radios[i].setOpaque(true);
            grupo.add(radios[i]);
            funcaoGrid.add(radios[i]);
            if (FUNCOES[i].equals(funcaoAtual)) radios[i].setSelected(true);
        }
        if (!editando) radios[0].setSelected(true);

        JPanel funcaoWrap = new JPanel(new BorderLayout());
        funcaoWrap.setOpaque(false);
        funcaoWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        funcaoWrap.add(funcaoGrid, BorderLayout.CENTER);

        // Botões
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botão excluir — só aparece na edição
        if (editando) {
            JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
            btnExcluir.setForeground(new Color(0xCC2222));
            btnExcluir.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(dialog,
                    "Deseja excluir o colaborador " + colaboradorExistente.getNomeCompleto() + "?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        ColaboradorController controller = ContextoAplicacao.getBean(ColaboradorController.class);
                        controller.delete(colaboradorExistente.getId());
                        dialog.dispose();
                        carregarColaboradores();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog,
                            "Erro ao excluir: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            rodape.add(btnExcluir);
        }

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            // Validação básica na view
            String nome     = txtNome.getText().trim();
            String cpf      = txtCPF.getText().trim();
            String admissao = txtAdmissao.getText().trim();
            String telefone = txtTelefone.getText().trim();
            String email    = txtEmail.getText().trim();
            String endereco = txtEndereco.getText().trim();
            String salarioStr = txtSalario.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || admissao.isEmpty()
                    || telefone.isEmpty() || email.isEmpty()
                    || endereco.isEmpty() || salarioStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Preencha todos os campos obrigatórios (*)",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dataAdmissao;
            try {
                dataAdmissao = LocalDate.parse(admissao, FMT_DATA);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Data de admissão inválida. Use o formato dd/mm/aaaa.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double salario;
            try {
                salario = Double.parseDouble(salarioStr.replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Salário inválido. Use apenas números (ex: 2500.00)",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Descobre qual função foi selecionada e busca ou cria no banco
            String funcaoSelecionada = "";
            for (JRadioButton r : radios) if (r.isSelected()) { funcaoSelecionada = r.getText(); break; }

            try {
                // Busca a FuncaoColaboradorModel pelo nome
                final IFuncaoColaboradorRepository funcaoRepo =
                    ContextoAplicacao.getBean(IFuncaoColaboradorRepository.class);
                final String funcaoFinal = funcaoSelecionada;
                FuncaoColaboradorModel funcaoModel = funcaoRepo
                    .findAll()
                    .stream()
                    .filter(f -> f.getFuncao().equalsIgnoreCase(funcaoFinal))
                    .findFirst()
                    .orElseGet(() -> {
                        // Cria a função se não existir
                        FuncaoColaboradorModel nova = new FuncaoColaboradorModel();
                        nova.setFuncao(funcaoFinal);
                        return funcaoRepo.save(nova);
                    });

                ColaboradorController controller = ContextoAplicacao.getBean(ColaboradorController.class);

                ColaboradorModel col = editando ? colaboradorExistente : new ColaboradorModel();
                col.setNomeCompleto(nome);
                if (!editando) col.setCpf(cpf);
                col.setDataAdmissao(dataAdmissao);
                col.setTelefone(telefone);
                col.setEmail(email);
                col.setEndereco(endereco);
                col.setSalario(salario);
                col.setFuncao(funcaoModel);

                if (editando) {
                    controller.update(col);
                } else {
                    controller.insert(col);
                }

                dialog.dispose();
                carregarColaboradores();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodape.add(btnCanc);
        rodape.add(btnSalv);

        form.add(grid);
        form.add(Box.createVerticalStrut(14));
        form.add(funcaoWrap);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
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
                g2.setColor(getForeground());
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