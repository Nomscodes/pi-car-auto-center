package br.com.picarauto.view;

/**
 * Cadastro de peças — integrado ao backend via PecaController.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
// NOVO: imports usados pelas máscaras (DocumentFilter) de preço, código e ano
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.PecaController;
import br.com.picarauto.controller.FornecedorController;
import br.com.picarauto.model.PecaModel;
import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;

public class PanelCadastroPeca extends JPanel {

    private final MainFrame frame;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;

    private List<PecaModel>       pecasAtuais;
    private List<FornecedorModel> fornecedoresDisponiveis;

    // Colunas: Código Nacional, Modelo, Marca, Ano Veículo, Preço, Garantia, Editar
    private static final String[] COLUNAS = {"Código", "Modelo", "Marca", "Ano Veículo", "Preço Unit.", "Garantia (m)", ""};

    // Ano de criação do primeiro carro do mundo (Benz Patent-Motorwagen, 1886) — limite inferior
    // para os campos "anoVeiculo" e "anoModelo". O limite superior é o ano atual.
    private static final int ANO_PRIMEIRO_VEICULO_FABRICADO = 1886;

    public PanelCadastroPeca(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_PECA), BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Peças");
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
                g2.setColor(MainFrame.COR_NAVY); g2.fillOval(0, 0, 30, 30);
                String car = new String(Character.toChars(0x1F697));
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(car, (30 - fm.stringWidth(car)) / 2, (30 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        av.setOpaque(false); av.setPreferredSize(new Dimension(30, 30));
        JLabel nome = new JLabel(MainFrame.getUsuarioLogado());
        nome.setFont(new Font("Segoe UI", Font.PLAIN, 12)); nome.setForeground(new Color(0xccddff));
        p.add(av); p.add(nome);
        return p;
    }

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
            BorderFactory.createLineBorder(new Color(0xD0C9B8)), BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setText("Pesquisar...");
        txtBusca.setForeground(Color.GRAY);
        txtBusca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("Pesquisar...".equals(txtBusca.getText())) { txtBusca.setText(""); txtBusca.setForeground(new Color(0x333333)); }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBusca.getText().isEmpty()) { txtBusca.setText("Pesquisar..."); txtBusca.setForeground(Color.GRAY); }
            }
        });
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        JButton btnNovaPeca = criarBotaoNavy("Nova peça", 110, 34);
        btnNovaPeca.addActionListener(e -> abrirFormPeca(null));

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);
        direita.add(btnNovaPeca);

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(direita, BorderLayout.EAST);

        cmbOrdenar = new JComboBox<>(new String[]{"Padrão", "Código ↑", "Código ↓", "Nome A-Z", "Nome Z-A"});
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
        if ("Código ↑".equals(sel))  sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Código ↓".equals(sel))  sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
        else if ("Nome A-Z".equals(sel)) sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        else if ("Nome Z-A".equals(sel)) sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(1, SortOrder.DESCENDING)));
        sorter.setRowFilter(hasText ? RowFilter.regexFilter("(?i)" + txt) : null);
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

        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new EditarRenderer());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (col == 6 && row >= 0 && pecasAtuais != null) {
                    int modelRow = tabela.convertRowIndexToModel(row);
                    if (modelRow < pecasAtuais.size())
                        abrirFormPeca(pecasAtuais.get(modelRow));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public void carregarPecas() {
        modelo.setRowCount(0);
        try {
            PecaController controller = ContextoAplicacao.getBean(PecaController.class);
            pecasAtuais = controller.findAll();
            for (PecaModel p : pecasAtuais) {
                modelo.addRow(new Object[]{
                    p.getCodigoNacional(),
                    p.getModelo(),
                    p.getMarca(),
                    p.getAnoVeiculo(),
                    String.format("R$ %.2f", p.getPrecoUnitario()),
                    p.getGarantia() + " m",
                    ""
                });
            }
            // Carrega fornecedores para o formulário
            fornecedoresDisponiveis = ContextoAplicacao.getBean(FornecedorController.class).findAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar peças: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormPeca(PecaModel existente) {
        boolean editando = existente != null;
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            editando ? "Editar Peça" : "Nova Peça",
            java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);

        // ALTERADO: campos abaixo agora usam métodos com máscara/restrição (criados nesta tela)
        JTextField txtCodigo    = criarCampoDigitos(6);   // ALTERADO: só dígitos, máx. 6 (Código Nacional)
        JTextField txtModeloPeca= criarCampo();
        JTextField txtMarcaPeca = criarCampo();
        JTextField txtAnoVeic   = criarCampoDigitos(4);   // ALTERADO: só dígitos, máx. 4 (Ano Veículo - AAAA)
        JTextField txtAnoMod    = criarCampoDigitos(4);   // ALTERADO: só dígitos, máx. 4 (Ano Modelo - AAAA)
        JTextField txtPreco     = criarCampoPreco();      // ALTERADO: máscara monetária R$ 0,00
        JTextField txtGarantia  = criarCampoDigitos(3);   // ALTERADO: só dígitos, máx. 3 (Garantia em meses)

        // Combo de fornecedor
        JComboBox<String> cmbFornecedor = new JComboBox<>();
        cmbFornecedor.setFont(MainFrame.FONT_NORMAL);
        cmbFornecedor.setBackground(Color.WHITE);
        if (fornecedoresDisponiveis != null)
            for (FornecedorModel f : fornecedoresDisponiveis)
                cmbFornecedor.addItem(f.getNomeFornecedor());

        if (editando) {
            txtCodigo.setText(String.valueOf(existente.getCodigoNacional()));
            txtCodigo.setEditable(false);
            txtCodigo.setBackground(new Color(0xEEEEEE));
            txtModeloPeca.setText(existente.getModelo());
            txtMarcaPeca.setText(existente.getMarca());
            txtAnoVeic.setText(String.valueOf(existente.getAnoVeiculo()));
            txtAnoMod.setText(String.valueOf(existente.getAnoModelo()));
            // ALTERADO: envia o valor em centavos (texto só com dígitos) para a máscara já formatar como R$ x,xx
            txtPreco.setText(String.valueOf(Math.round(existente.getPrecoUnitario() * 100)));
            txtGarantia.setText(String.valueOf(existente.getGarantia()));
            // Seleciona o fornecedor atual
            if (fornecedoresDisponiveis != null) {
                for (int i = 0; i < fornecedoresDisponiveis.size(); i++) {
                    if (fornecedoresDisponiveis.get(i).getId().equals(existente.getIdFornecedor())) {
                        cmbFornecedor.setSelectedIndex(i); break;
                    }
                }
            }
        }

        JPanel grid = new JPanel(new GridLayout(4, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 270));
        // ALTERADO: labels com indicação do formato esperado (6 dígitos / AAAA)
        grid.add(criarGrupo("Código Nacional * (6 dígitos)", txtCodigo));
        grid.add(criarGrupo("Modelo da peça *", txtModeloPeca));
        grid.add(criarGrupo("Marca da peça *", txtMarcaPeca));
        grid.add(criarGrupo("Ano veículo * (AAAA)", txtAnoVeic));
        grid.add(criarGrupo("Ano modelo * (AAAA)", txtAnoMod));
        grid.add(criarGrupo("Preço unitário *", txtPreco));
        grid.add(criarGrupo("Garantia (meses) *", txtGarantia));

        // Grupo do combo de fornecedor
        JPanel grupoForn = new JPanel();
        grupoForn.setOpaque(false);
        grupoForn.setLayout(new BoxLayout(grupoForn, BoxLayout.Y_AXIS));
        JLabel lblForn = new JLabel("Fornecedor *");
        lblForn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblForn.setForeground(new Color(0x444444));
        lblForn.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbFornecedor.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbFornecedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        grupoForn.add(lblForn); grupoForn.add(Box.createVerticalStrut(4)); grupoForn.add(cmbFornecedor);
        grid.add(grupoForn);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (editando) {
            JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
            btnExcluir.setForeground(new Color(0xCC2222));
            btnExcluir.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(dialog, "Deseja excluir esta peça?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        ContextoAplicacao.getBean(PecaController.class).delete(existente.getId());
                        dialog.dispose(); carregarPecas();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            rodape.add(btnExcluir);
        }

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            try {
                // NOVO: validação de Código Nacional com exatamente 6 dígitos
                String codigoStr = txtCodigo.getText().trim();
                if (codigoStr.length() != 6) {
                    JOptionPane.showMessageDialog(dialog,
                        "O código nacional deve conter exatamente 6 números.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // NOVO: validação de Ano Veículo/Modelo com exatamente 4 dígitos
                String anoVStr = txtAnoVeic.getText().trim();
                String anoMStr = txtAnoMod.getText().trim();
                if (anoVStr.length() != 4 || anoMStr.length() != 4) {
                    JOptionPane.showMessageDialog(dialog,
                        "Os campos de ano devem ser preenchidos com 4 dígitos (AAAA).",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int codigo = Integer.parseInt(codigoStr);
                String mod   = txtModeloPeca.getText().trim();
                String marc  = txtMarcaPeca.getText().trim();
                int anoV     = Integer.parseInt(anoVStr);
                int anoM     = Integer.parseInt(anoMStr);
                // ALTERADO: preço agora é extraído do texto mascarado (R$ x.xxx,xx) em vez de Double.parseDouble direto
                double preco = extrairValorPreco(txtPreco.getText());

                String garantStr = txtGarantia.getText().trim();
                if (garantStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Informe a garantia em meses (apenas números, 0 ou maior).",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int garant = Integer.parseInt(garantStr);
                if (garant < 0) {
                    JOptionPane.showMessageDialog(dialog,
                        "A garantia não pode ser negativa. Informe um número de 0 ou maior.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int anoAtual = java.time.LocalDate.now().getYear();

                // NOVO: ano do veículo deve estar entre a criação do primeiro carro do mundo e o ano atual
                if (anoV < ANO_PRIMEIRO_VEICULO_FABRICADO || anoV > anoAtual) {
                    JOptionPane.showMessageDialog(dialog,
                        "O ano do veículo deve estar entre " + ANO_PRIMEIRO_VEICULO_FABRICADO
                            + " e " + anoAtual + ".",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // NOVO: mesma regra para o ano do modelo
                if (anoM < ANO_PRIMEIRO_VEICULO_FABRICADO || anoM > anoAtual) {
                    JOptionPane.showMessageDialog(dialog,
                        "O ano do modelo deve estar entre " + ANO_PRIMEIRO_VEICULO_FABRICADO
                            + " e " + anoAtual + ".",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (mod.isEmpty() || marc.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (cmbFornecedor.getSelectedIndex() < 0 || fornecedoresDisponiveis == null || fornecedoresDisponiveis.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Selecione um fornecedor.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Long idForn = fornecedoresDisponiveis.get(cmbFornecedor.getSelectedIndex()).getId();
                PecaController controller = ContextoAplicacao.getBean(PecaController.class);
                PecaModel p = editando ? existente : new PecaModel();
                if (!editando) p.setCodigoNacional(codigo);
                p.setModelo(mod);
                p.setMarca(marc);
                p.setAnoVeiculo(anoV);
                p.setAnoModelo(anoM);
                p.setPrecoUnitario(preco);
                p.setGarantia(garant);
                p.setIdFornecedor(idForn);

                if (editando) controller.update(p);
                else          controller.insert(p);

                dialog.dispose(); carregarPecas();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Verifique os campos numéricos (código, ano, preço, garantia).", "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (FieldValidationException | RuleValidationException valEx) {
                JOptionPane.showMessageDialog(dialog, valEx.getMessage(), "Erro de validação", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodape.add(btnCanc); rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(grid);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }

    private JPanel criarGrupo(String label, JTextField campo) {
        JPanel g = new JPanel(); g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11)); lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        g.add(lbl); g.add(Box.createVerticalStrut(4)); g.add(campo);
        return g;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField(); f.setFont(MainFrame.FONT_NORMAL); f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1), new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34)); return f;
    }

    // ===================== NOVO: bloco de campos/máscaras para Código, Ano e Preço =====================

    /** NOVO: Campo numérico que aceita apenas dígitos, limitado a {@code maxDigitos} caracteres. */
    private JTextField criarCampoDigitos(int maxDigitos) {
        JTextField f = criarCampo();
        ((javax.swing.text.AbstractDocument) f.getDocument()).setDocumentFilter(new DigitLimitFilter(maxDigitos));
        return f;
    }

    /** NOVO: Campo de preço com máscara monetária no formato R$ 0,00, atualizada conforme o usuário digita. */
    private JTextField criarCampoPreco() {
        JTextField f = criarCampo();
        ((javax.swing.text.AbstractDocument) f.getDocument()).setDocumentFilter(new CurrencyFilter());
        f.setText("0");
        return f;
    }

    /** NOVO: Converte o texto mascarado (ex: "R$ 1.234,56") para o valor double correspondente (1234.56). */
    private double extrairValorPreco(String textoMascarado) {
        String digitos = textoMascarado == null ? "" : textoMascarado.replaceAll("\\D", "");
        if (digitos.isEmpty()) return 0.0;
        long centavos = Long.parseLong(digitos);
        return centavos / 100.0;
    }

    /**
     * NOVO: Filtro que restringe um campo a apenas dígitos numéricos, com um limite máximo de caracteres.
     * Usado para Código Nacional (6 dígitos) e Ano Veículo / Ano Modelo (4 dígitos).
     */
    private static class DigitLimitFilter extends DocumentFilter {
        private final int maxDigitos;

        DigitLimitFilter(int maxDigitos) {
            this.maxDigitos = maxDigitos;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String digitos = text == null ? "" : text.replaceAll("\\D", "");
            int tamanhoAtual = fb.getDocument().getLength();
            int tamanhoFinal = tamanhoAtual - length + digitos.length();
            if (tamanhoFinal > maxDigitos) {
                int permitido = maxDigitos - (tamanhoAtual - length);
                if (permitido <= 0) {
                    digitos = "";
                } else {
                    digitos = digitos.substring(0, Math.min(permitido, digitos.length()));
                }
            }
            super.replace(fb, offset, length, digitos, attrs);
        }
    }

    /**
     * NOVO: Filtro que aplica máscara monetária no padrão brasileiro (R$ 0,00) enquanto o usuário digita.
     * Os dígitos informados são tratados como centavos, da direita para a esquerda — semelhante
     * ao comportamento de campos de valor em caixas eletrônicos / apps bancários.
     */
    private static class CurrencyFilter extends DocumentFilter {
        private static final DecimalFormat FORMATO =
            new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        private static final int MAX_DIGITOS = 11; // suporta até R$ 999.999.999,99

        private StringBuilder centavos = new StringBuilder("0");

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            replace(fb, offset, length, "", null);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String digitosInseridos = text == null ? "" : text.replaceAll("\\D", "");
            boolean houveRemocao = length > 0 && digitosInseridos.isEmpty();

            if (houveRemocao) {
                // Backspace/Delete: remove o último dígito (centavo) do valor atual
                if (centavos.length() > 1) {
                    centavos.deleteCharAt(centavos.length() - 1);
                } else {
                    centavos.setLength(0);
                    centavos.append("0");
                }
            } else if (!digitosInseridos.isEmpty()) {
                // Remove zero à esquerda "sozinho" antes de acrescentar novos dígitos
                if (centavos.length() == 1 && centavos.charAt(0) == '0') {
                    centavos.setLength(0);
                }
                centavos.append(digitosInseridos);
                if (centavos.length() > MAX_DIGITOS) {
                    centavos.delete(0, centavos.length() - MAX_DIGITOS);
                }
            }

            String formatado = formatar();
            super.replace(fb, 0, fb.getDocument().getLength(), formatado, attrs);
        }

        private String formatar() {
            String valorCentavos = centavos.toString();
            if (valorCentavos.isEmpty()) valorCentavos = "0";
            long total = Long.parseLong(valorCentavos);
            return "R$ " + FORMATO.format(total / 100.0);
        }
    }

    // ===================== FIM DO BLOCO NOVO =====================

    private JButton criarBotaoNavy(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h)); return btn;
    }

    private JButton criarBotaoGold(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY); g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h)); return btn;
    }

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground()); g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setForeground(MainFrame.COR_NAVY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h)); return btn;
    }

    static class EditarRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Editar", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11)); lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true); lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}