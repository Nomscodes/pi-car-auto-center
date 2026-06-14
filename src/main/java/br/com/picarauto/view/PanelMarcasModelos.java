package br.com.picarauto.view;

/**
 * Gerenciamento de marcas e modelos — abas Marcas / Modelos com busca e tabela.
 *
 * ALTERAÇÕES em relação à versão original (Cassiano):
 *  - Removidos arrays estáticos DADOS_MARCAS / DADOS_MODELOS
 *  - Integração real com MarcaController e ModeloController via ContextoAplicacao
 *  - recarregarTabela() carrega do banco
 *  - Combos de marca populados do banco
 *  - Validação de campos vazios e de ano nos formulários
 *  - Mouse listener na coluna "Editar" abre diálogo de edição/exclusão
 *
 * @author Cassiano 
 */

import br.com.picarauto.controller.MarcaController;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;
import br.com.picarauto.util.ContextoAplicacao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PanelMarcasModelos extends JPanel {

    private final MainFrame frame;

    private boolean abaMarcas = true;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;
    private JButton btnAbaMarcas;
    private JButton btnAbaModelos;

    private List<MarcaModel>  listaMarcas  = new ArrayList<>();
    private List<ModeloModel> listaModelos = new ArrayList<>();

    private static final String[] COLUNAS_MARCAS  = {"Marca", "Modelos cadastrados", ""};
    private static final String[] COLUNAS_MODELOS = {"Modelo", "Marca", "Ano", ""};

    public PanelMarcasModelos(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private MarcaController getMarcaController() {
        return ContextoAplicacao.getBean(MarcaController.class);
    }

    private ModeloController getModeloController() {
        return ContextoAplicacao.getBean(ModeloController.class);
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_MARCAS_MOD), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Marcas e Modelos");
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

    private JPanel criarConteudo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainFrame.COR_CREAM);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarBarraFerr() {
        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);

        btnAbaMarcas  = criarBotaoAba("Marcas");
        btnAbaModelos = criarBotaoAba("Modelos");
        btnAbaMarcas.addActionListener(e -> {
            abaMarcas = true;
            recarregarTabela();
            if (cmbOrdenar != null) cmbOrdenar.setSelectedIndex(0);
            btnAbaMarcas.repaint(); btnAbaModelos.repaint();
        });
        btnAbaModelos.addActionListener(e -> {
            abaMarcas = false;
            recarregarTabela();
            if (cmbOrdenar != null) cmbOrdenar.setSelectedIndex(0);
            btnAbaMarcas.repaint(); btnAbaModelos.repaint();
        });
        abas.add(btnAbaMarcas);
        abas.add(btnAbaModelos);

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

        JButton btnNovaMarca  = criarBotaoNavy("Nova marca",  120, 34);
        JButton btnNovoModelo = criarBotaoNavy("Novo modelo", 130, 34);
        btnNovaMarca.addActionListener(e -> abrirFormNovaMarca());
        btnNovoModelo.addActionListener(e -> abrirFormNovoModelo());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnNovaMarca);
        btnPanel.add(btnNovoModelo);

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnPanel, BorderLayout.EAST);

        cmbOrdenar = new JComboBox<>(new String[]{"Padrão", "A-Z", "Z-A"});
        cmbOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbOrdenar.setBackground(Color.WHITE);
        cmbOrdenar.setPreferredSize(new Dimension(160, 32));
        cmbOrdenar.addActionListener(e -> aplicarFiltros());

        JPanel filtroRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        filtroRow.setOpaque(false);
        filtroRow.add(cmbOrdenar);

        JPanel meio = new JPanel(new BorderLayout(0, 6));
        meio.setOpaque(false);
        meio.add(painelBusca, BorderLayout.NORTH);
        meio.add(filtroRow,   BorderLayout.SOUTH);

        JPanel barra = new JPanel(new BorderLayout(0, 10));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        barra.add(abas, BorderLayout.NORTH);
        barra.add(meio, BorderLayout.SOUTH);
        return barra;
    }

    private JButton criarBotaoAba(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = (texto.equals("Marcas") && abaMarcas)
                             || (texto.equals("Modelos") && !abaMarcas);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(ativo ? Color.WHITE : MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 34));
        return btn;
    }

    private void aplicarFiltros() {
        if (sorter == null) return;
        String sel = cmbOrdenar == null ? "Padrão" : (String) cmbOrdenar.getSelectedItem();
        if (sel == null) sel = "Padrão";
        String txt = txtBusca.getText().trim();
        boolean hasText = !txt.isEmpty() && !"Pesquisar...".equals(txt);

        sorter.setSortKeys(java.util.Collections.emptyList());
        if ("A-Z".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Z-A".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        sorter.setRowFilter(hasText ? RowFilter.regexFilter("(?i)" + txt) : null);
    }

    private JScrollPane criarScrollTabela() {
        String[] cols = abaMarcas ? COLUNAS_MARCAS : COLUNAS_MODELOS;
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        recarregarTabela();

        tabela = new JTable(modelo);
        sorter  = new TableRowSorter<>(modelo);
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

        int lastCol = tabela.getColumnCount() - 1;
        tabela.getColumnModel().getColumn(lastCol).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(lastCol).setCellRenderer(new EditarRenderer());

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int viewRow = tabela.rowAtPoint(e.getPoint());
                int viewCol = tabela.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol != tabela.getColumnCount() - 1) return;

                int modelRow = tabela.convertRowIndexToModel(viewRow);

                if (abaMarcas && modelRow < listaMarcas.size())
                    abrirDialogEditarMarca(listaMarcas.get(modelRow));
                else if (!abaMarcas && modelRow < listaModelos.size())
                    abrirDialogEditarModelo(listaModelos.get(modelRow));
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void recarregarTabela() {
        if (modelo == null) return;
        modelo.setRowCount(0);
        listaMarcas.clear();
        listaModelos.clear();

        try {
            if (abaMarcas) {
                listaMarcas = new ArrayList<>(getMarcaController().findAll());
                ModeloController mc = getModeloController();
                for (MarcaModel m : listaMarcas) {
                    List<ModeloModel> modsPorMarca = mc.findAllByIdMarca(m.getId());
                    String nomesModelos = modsPorMarca.stream()
                            .map(ModeloModel::getNomeModelo)
                            .collect(Collectors.joining(", "));
                    modelo.addRow(new Object[]{
                        m.getNome(),
                        nomesModelos.isEmpty() ? "—" : nomesModelos,
                        ""
                    });
                }
            } else {
                listaModelos = new ArrayList<>(getModeloController().findAll());
                Map<Long, String> nomesMarca = getMarcaController().findAll().stream()
                        .collect(Collectors.toMap(MarcaModel::getId, MarcaModel::getNome));
                for (ModeloModel m : listaModelos) {
                    modelo.addRow(new Object[]{
                        m.getNomeModelo(),
                        nomesMarca.getOrDefault(m.getIdMarca(), "—"),
                        m.getAnoModelo(),
                        ""
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados do banco:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Nova Marca — sigla só visual, não vai ao banco ────────────────────────
    private void abrirFormNovaMarca() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Nova Marca", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 220);
        dialog.setLocationRelativeTo(this);

        JTextField txtNome  = criarCampo();
        JTextField txtSigla = criarCampo(); // apenas visual, não salvo no banco

        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        grid.add(criarGrupo("Nome da marca", txtNome));
        grid.add(criarGrupo("Sigla (ex.: GM)", txtSigla));

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            String nome = txtNome.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O nome da marca é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }

            try {
                MarcaModel novaMarca = new MarcaModel();
                novaMarca.setNome(capitalizarPalavras(nome)); // sigla não é enviada ao banco
                getMarcaController().insert(novaMarca);

                abaMarcas = true;
                recarregarTabela();
                btnAbaMarcas.repaint();
                btnAbaModelos.repaint();
                dialog.dispose();

            } catch (FieldValidationException | RuleValidationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao salvar marca:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
        form.add(grid);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    // ── Novo Modelo ───────────────────────────────────────────────────────────
    private void abrirFormNovoModelo() {

        List<MarcaModel> marcasDispBD;
        try {
            marcasDispBD = getMarcaController().findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar marcas:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (marcasDispBD.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Cadastre ao menos uma marca antes de adicionar um modelo.",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Novo Modelo", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(440, 280);
        dialog.setLocationRelativeTo(this);

        final List<MarcaModel> marcasRef = marcasDispBD;

        String[] nomesMarcas = marcasRef.stream()
                .map(MarcaModel::getNome)
                .toArray(String[]::new);

        JComboBox<String> cmbMarca = new JComboBox<>(nomesMarcas);
        cmbMarca.setFont(MainFrame.FONT_NORMAL);
        cmbMarca.setBackground(Color.WHITE);
        cmbMarca.setPreferredSize(new Dimension(0, 34));
        cmbMarca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        if (abaMarcas && tabela.getSelectedRow() >= 0) {
            int modelRow = tabela.convertRowIndexToModel(tabela.getSelectedRow());
            if (modelRow < listaMarcas.size()) {
                String nomeSel = listaMarcas.get(modelRow).getNome();
                cmbMarca.setSelectedItem(nomeSel);
            }
        }

        JTextField txtNome = criarCampo();

        JTextField txtAno = criarCampo();
        int anoMax = LocalDate.now().getYear() + 1;
        txtAno.setToolTipText("Entre 1900 e " + anoMax);

        JPanel grupoMarca = new JPanel();
        grupoMarca.setOpaque(false);
        grupoMarca.setLayout(new BoxLayout(grupoMarca, BoxLayout.Y_AXIS));
        JLabel lblMarca = new JLabel("Marca *");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMarca.setForeground(new Color(0x444444));
        lblMarca.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMarca.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupoMarca.add(lblMarca);
        grupoMarca.add(Box.createVerticalStrut(4));
        grupoMarca.add(cmbMarca);

        JPanel gridTop = new JPanel(new GridLayout(1, 2, 14, 0));
        gridTop.setOpaque(false);
        gridTop.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        gridTop.add(criarGrupo("Nome do modelo *", txtNome));
        gridTop.add(grupoMarca);

        JPanel gridBot = new JPanel(new GridLayout(1, 2, 14, 0));
        gridBot.setOpaque(false);
        gridBot.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridBot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        gridBot.add(criarGrupo("Ano do modelo * (ex: " + anoMax + ")", txtAno));
        gridBot.add(new JPanel() {{ setOpaque(false); }});

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            String nome   = txtNome.getText().trim();
            String anoTxt = txtAno.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O nome do modelo é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }
            if (anoTxt.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O ano do modelo é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }

            int anoValor;
            try {
                anoValor = Integer.parseInt(anoTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "O ano deve conter apenas números inteiros (ex: 2025).",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }

            if (anoValor < 1900 || anoValor > anoMax) {
                JOptionPane.showMessageDialog(dialog,
                    "Ano inválido. Informe um valor entre 1900 e " + anoMax + ".",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }

            try {
                int idxMarca = cmbMarca.getSelectedIndex();
                MarcaModel marcaSel = marcasRef.get(idxMarca);

                ModeloModel novoModelo = new ModeloModel();
                novoModelo.setNomeModelo(capitalizarPalavras(nome));
                novoModelo.setAnoModelo(anoValor);
                novoModelo.setIdMarca(marcaSel.getId());

                getModeloController().insert(novoModelo);

                abaMarcas = false;
                recarregarTabela();
                btnAbaMarcas.repaint();
                btnAbaModelos.repaint();
                dialog.dispose();

            } catch (FieldValidationException | RuleValidationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao salvar modelo:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
        form.add(gridTop);
        form.add(Box.createVerticalStrut(10));
        form.add(gridBot);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    // ── Editar / Excluir Marca ────────────────────────────────────────────────
    private void abrirDialogEditarMarca(MarcaModel marcaAtual) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Editar Marca", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 230);
        dialog.setLocationRelativeTo(this);

        JTextField txtNome = criarCampo();
        txtNome.setText(marcaAtual.getNome());

        JPanel grid = new JPanel(new GridLayout(1, 1, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        grid.add(criarGrupo("Nome da marca *", txtNome));

        JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
        btnExcluir.setForeground(new Color(0xCC3333));
        btnExcluir.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(dialog,
                "Deseja excluir \"" + marcaAtual.getNome() + "\"?\nO registro será desativado.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    getMarcaController().delete(marcaAtual.getId());
                    recarregarTabela();
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erro ao excluir:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O nome da marca é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }
            try {
                marcaAtual.setNome(nome);
                getMarcaController().update(marcaAtual);
                recarregarTabela();
                dialog.dispose();
            } catch (FieldValidationException | RuleValidationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao atualizar:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        rodape.add(btnExcluir);
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

    // ── Editar / Excluir Modelo ───────────────────────────────────────────────
    private void abrirDialogEditarModelo(ModeloModel modeloAtual) {
        List<MarcaModel> marcasDispBD;
        try {
            marcasDispBD = getMarcaController().findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar marcas:\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Editar Modelo", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(440, 300);
        dialog.setLocationRelativeTo(this);

        final List<MarcaModel> marcasRef = marcasDispBD;
        String[] nomesMarcas = marcasRef.stream().map(MarcaModel::getNome).toArray(String[]::new);

        JComboBox<String> cmbMarca = new JComboBox<>(nomesMarcas);
        cmbMarca.setFont(MainFrame.FONT_NORMAL);
        cmbMarca.setBackground(Color.WHITE);
        cmbMarca.setPreferredSize(new Dimension(0, 34));
        cmbMarca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        for (int i = 0; i < marcasRef.size(); i++) {
            if (marcasRef.get(i).getId().equals(modeloAtual.getIdMarca())) {
                cmbMarca.setSelectedIndex(i);
                break;
            }
        }

        JTextField txtNome = criarCampo();
        txtNome.setText(modeloAtual.getNomeModelo());

        int anoMax = LocalDate.now().getYear() + 1;
        JTextField txtAno = criarCampo();
        txtAno.setText(modeloAtual.getAnoModelo() != null
            ? String.valueOf(modeloAtual.getAnoModelo()) : "");
        txtAno.setToolTipText("Entre 1900 e " + anoMax);

        JPanel grupoMarca = new JPanel();
        grupoMarca.setOpaque(false);
        grupoMarca.setLayout(new BoxLayout(grupoMarca, BoxLayout.Y_AXIS));
        JLabel lblMarca = new JLabel("Marca *");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMarca.setForeground(new Color(0x444444));
        lblMarca.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMarca.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupoMarca.add(lblMarca);
        grupoMarca.add(Box.createVerticalStrut(4));
        grupoMarca.add(cmbMarca);

        JPanel gridTop = new JPanel(new GridLayout(1, 2, 14, 0));
        gridTop.setOpaque(false);
        gridTop.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        gridTop.add(criarGrupo("Nome do modelo *", txtNome));
        gridTop.add(grupoMarca);

        JPanel gridBot = new JPanel(new GridLayout(1, 2, 14, 0));
        gridBot.setOpaque(false);
        gridBot.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridBot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        gridBot.add(criarGrupo("Ano do modelo * (ex: " + anoMax + ")", txtAno));
        gridBot.add(new JPanel() {{ setOpaque(false); }});

        JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
        btnExcluir.setForeground(new Color(0xCC3333));
        btnExcluir.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(dialog,
                "Deseja excluir \"" + modeloAtual.getNomeModelo() + "\"?\nO registro será desativado.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    getModeloController().delete(modeloAtual.getId());
                    recarregarTabela();
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erro ao excluir:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCanc = criarBotaoOutline("Cancelar", 100, 34);
        btnCanc.addActionListener(e -> dialog.dispose());

        JButton btnSalv = criarBotaoGold("Salvar", 100, 34);
        btnSalv.addActionListener(e -> {
            String nome   = txtNome.getText().trim();
            String anoTxt = txtAno.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O nome do modelo é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }
            if (anoTxt.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "O ano do modelo é obrigatório.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }
            int anoValor;
            try {
                anoValor = Integer.parseInt(anoTxt);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "O ano deve conter apenas números inteiros.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }
            if (anoValor < 1900 || anoValor > anoMax) {
                JOptionPane.showMessageDialog(dialog,
                    "Ano inválido. Informe um valor entre 1900 e " + anoMax + ".",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
                txtAno.requestFocus();
                return;
            }

            try {
                int idxMarca = cmbMarca.getSelectedIndex();
                modeloAtual.setNomeModelo(nome);
                modeloAtual.setAnoModelo(anoValor);
                modeloAtual.setIdMarca(marcasRef.get(idxMarca).getId());

                getModeloController().update(modeloAtual);
                recarregarTabela();
                dialog.dispose();

            } catch (FieldValidationException | RuleValidationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Erro ao atualizar:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.setAlignmentX(Component.LEFT_ALIGNMENT);
        rodape.add(btnExcluir);
        rodape.add(btnCanc);
        rodape.add(btnSalv);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(gridTop);
        form.add(Box.createVerticalStrut(10));
        form.add(gridBot);
        form.add(Box.createVerticalStrut(14));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }

    // ── Helper de capitalização ───────────────────────────────────────────────
    private String capitalizarPalavras(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        String[] palavras = texto.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : palavras) {
            if (!p.isEmpty()) {
                sb.append(Character.toUpperCase(p.charAt(0)))
                  .append(p.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
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