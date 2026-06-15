package br.com.picarauto.view;

/**
 * Lista e cadastro de fornecedores — integrado ao backend.
 * Máscaras em tempo real: CNPJ (00.000.000/0000-00) e telefone ((00) 00000-0000).
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
import java.util.List;
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.FornecedorController;
import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;

public class PanelCadastroFornecedor extends JPanel {

    private final MainFrame frame;

    private JTextField    txtBusca;
    private JTable        tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;

    private List<FornecedorModel> fornecedoresAtuais;

    private static final String[] COLUNAS = {"Razão Social", "CNPJ", "Telefone", "E-mail", ""};

    public PanelCadastroFornecedor(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_FORNECEDOR), BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Fornecedores");
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

        // BOTÃO TELA CADASTRO FORNECEDOR DE ABRIR DIALOG NOVO FORNECEDOR
        JButton btnNovo = criarBotaoNavy("Novo fornecedor", 150, 34);
        btnNovo.addActionListener(e -> abrirFormFornecedor(null));

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);

        cmbOrdenar = new JComboBox<>(new String[]{"Padrão", "A-Z (Nome)", "Z-A (Nome)"});
        cmbOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbOrdenar.setBackground(Color.WHITE);
        cmbOrdenar.setPreferredSize(new Dimension(180, 32));
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
        sorter.setRowFilter(hasText ? RowFilter.regexFilter("(?i)" + txt) : null);
    }

    // ── Tabela ────────────────────────────────────────────────────────────────
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

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (col == 4 && row >= 0 && fornecedoresAtuais != null) {
                    int modelRow = tabela.convertRowIndexToModel(row);
                    if (modelRow < fornecedoresAtuais.size())
                        abrirFormFornecedor(fornecedoresAtuais.get(modelRow));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // ── Carrega do banco ───────────────────────────────────────────────────────
    public void carregarFornecedores() {
        modelo.setRowCount(0);
        try {
            FornecedorController controller = ContextoAplicacao.getBean(FornecedorController.class);
            fornecedoresAtuais = controller.findAll();
            for (FornecedorModel f : fornecedoresAtuais) {
                modelo.addRow(new Object[]{
                    f.getNomeFornecedor(),
                    f.getCnpj() != null ? formatarCnpj(f.getCnpj()) : "-",
                    formatarTelefone(f.getTelefone()),
                    f.getEmail(),
                    ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar fornecedores: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Diálogo cadastro / edição ─────────────────────────────────────────────
    private void abrirFormFornecedor(FornecedorModel existente) {
        boolean editando = existente != null;
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            editando ? "Editar Fornecedor" : "Novo Fornecedor",
            java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(460, 360);
        dialog.setLocationRelativeTo(this);

        JTextField txtRazao    = criarCampo();
        JTextField txtCNPJ     = criarCampo();
        JTextField txtTelefone = criarCampo();
        JTextField txtEmail    = criarCampo();

        // Aplica máscaras em tempo real
        aplicarMascaraCnpj(txtCNPJ);
        aplicarMascaraTelefone(txtTelefone);
        aplicarMascaraCnpj(txtCNPJ);
        aplicarMascaraTelefone(txtTelefone);
        aplicarCapitalizacaoNome(txtRazao);

        // Preenche os campos se for edição
        if (editando) {
            txtRazao.setText(existente.getNomeFornecedor());
            // Exibe CNPJ já formatado; o campo é somente leitura
            if (existente.getCnpj() != null && !existente.getCnpj().isBlank())
                txtCNPJ.setText(formatarCnpj(existente.getCnpj()));
            txtCNPJ.setEditable(false);
            txtCNPJ.setBackground(new Color(0xEEEEEE));
            // Exibe telefone formatado
            txtTelefone.setText(formatarTelefone(existente.getTelefone()));
            txtEmail.setText(existente.getEmail());
        }

        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        grid.add(criarGrupo("Razão Social *",      txtRazao));
        grid.add(criarGrupo("CNPJ",     txtCNPJ));
        grid.add(criarGrupo("Telefone *",          txtTelefone));
        grid.add(criarGrupo("E-mail *",            txtEmail));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (editando) {
            // BOTÃO DIALOG CADASTRO FORNECEDOR DE EXCLUIR FORNECEDOR
            JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
            btnExcluir.setForeground(new Color(0xCC2222));
            btnExcluir.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(dialog,
                    "Deseja excluir o fornecedor " + existente.getNomeFornecedor() + "?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        ContextoAplicacao.getBean(FornecedorController.class).delete(existente.getId());
                        dialog.dispose();
                        carregarFornecedores();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog,
                            "Erro ao excluir: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            rodape.add(btnExcluir);
        }

        // BOTÃO DIALOG CADASTRO FORNECEDOR DE CANCELAR
        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        // BOTÃO DIALOG CADASTRO FORNECEDOR DE SALVAR FORNECEDOR NO BANCO
        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            String razao    = txtRazao.getText().trim();
            // Remove máscara antes de enviar ao backend
            String cnpj     = txtCNPJ.getText().replaceAll("\\D", "").trim();
            String telefone = txtTelefone.getText().replaceAll("\\D", "").trim();
            String email    = txtEmail.getText().trim();

            if (razao.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Preencha todos os campos obrigatórios (*).",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                FornecedorController controller = ContextoAplicacao.getBean(FornecedorController.class);
                FornecedorModel f = editando ? existente : new FornecedorModel();
                f.setNomeFornecedor(razao);
                if (!editando) f.setCnpj(cnpj.isEmpty() ? null : cnpj);
                // Envia telefone só com dígitos; a validation do backend não formata telefone
                // de fornecedor (sem helper de formatação lá), então guardamos o dígito puro
                f.setTelefone(telefone);
                f.setEmail(email);
                if (editando) controller.update(f);
                else          controller.insert(f);
                dialog.dispose();
                carregarFornecedores();
            } catch (FieldValidationException | RuleValidationException valEx) {
                JOptionPane.showMessageDialog(dialog,
                    valEx.getMessage(), "Erro de validação", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(grid);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    // ── Máscaras em tempo real ────────────────────────────────────────────────

    /**
     * Máscara de CNPJ: 00.000.000/0000-00
     * Aceita apenas dígitos e formata automaticamente.
     */
    private void aplicarMascaraCnpj(JTextField campo) {
        campo.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String apenasDigitos = str.replaceAll("\\D", "");
                String atual = getText(0, getLength()).replaceAll("\\D", "");
                if (atual.length() >= 14) return;
                int espaco = 14 - atual.length();
                if (apenasDigitos.length() > espaco)
                    apenasDigitos = apenasDigitos.substring(0, espaco);
                if (!apenasDigitos.isEmpty())
                    super.insertString(offs, apenasDigitos, a);
            }
        });
        campo.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;
            private void formatar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    String raw = campo.getText().replaceAll("\\D", "");
                    if (raw.length() > 14) raw = raw.substring(0, 14);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < raw.length(); i++) {
                        if (i == 2 || i == 5) sb.append('.');
                        if (i == 8) sb.append('/');
                        if (i == 12) sb.append('-');
                        sb.append(raw.charAt(i));
                    }
                    campo.setText(sb.toString());
                    campo.setCaretPosition(campo.getText().length());
                    atualizando = false;
                });
            }
            @Override public void insertUpdate(DocumentEvent e)  { formatar(); }
            @Override public void removeUpdate(DocumentEvent e)  { formatar(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
    }

    /**
     * Máscara de telefone: (00) 00000-0000 ou (00) 0000-0000
     */
    private void aplicarMascaraTelefone(JTextField campo) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;
            private void formatar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    String raw = campo.getText().replaceAll("\\D", "");
                    if (raw.length() > 11) raw = raw.substring(0, 11);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < raw.length(); i++) {
                        if (i == 0) sb.append('(');
                        if (i == 2) sb.append(") ");
                        boolean celular = raw.length() == 11;
                        if ((celular && i == 7) || (!celular && i == 6)) sb.append('-');
                        sb.append(raw.charAt(i));
                    }
                    campo.setText(sb.toString());
                    campo.setCaretPosition(campo.getText().length());
                    atualizando = false;
                });
            }
            @Override public void insertUpdate(DocumentEvent e)  { formatar(); }
            @Override public void removeUpdate(DocumentEvent e)  { formatar(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    // ── Capitalização da razão social ─────────────────────────────────────────
    private void aplicarCapitalizacaoNome(JTextField campo) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;

            @Override public void insertUpdate(DocumentEvent e)  { capitalizar(); }
            @Override public void removeUpdate(DocumentEvent e)  {}
            @Override public void changedUpdate(DocumentEvent e) {}

            private void capitalizar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    try {
                        String texto = campo.getText();
                        if (!texto.isEmpty()) {
                            String capitalizado = Character.toUpperCase(texto.charAt(0))
                                    + texto.substring(1);
                            if (!capitalizado.equals(texto)) {
                                int caret = campo.getCaretPosition();
                                campo.setText(capitalizado);
                                campo.setCaretPosition(Math.min(caret, capitalizado.length()));
                            }
                        }
                    } finally {
                        atualizando = false;
                    }
                });
            }
        });
    }

    // ── Helpers de formatação para exibição na tabela e no form de edição ─────

    /**
     * Formata CNPJ com 14 dígitos: 00.000.000/0000-00
     */
    private String formatarCnpj(String cnpj) {
        if (cnpj == null) return "";
        String d = cnpj.replaceAll("\\D", "");
        if (d.length() != 14) return cnpj;
        return d.substring(0, 2) + "." + d.substring(2, 5) + "." + d.substring(5, 8)
             + "/" + d.substring(8, 12) + "-" + d.substring(12);
    }

    /**
     * Formata telefone com DDD.
     * 11 dígitos (celular): (00) 00000-0000
     * 10 dígitos (fixo):    (00) 0000-0000
     */
    private String formatarTelefone(String telefone) {
        if (telefone == null) return "";
        String d = telefone.replaceAll("\\D", "");
        if (d.length() == 11)
            return "(" + d.substring(0, 2) + ") " + d.substring(2, 7) + "-" + d.substring(7);
        if (d.length() == 10)
            return "(" + d.substring(0, 2) + ") " + d.substring(2, 6) + "-" + d.substring(6);
        return telefone;
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────
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
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setForeground(MainFrame.COR_NAVY);
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