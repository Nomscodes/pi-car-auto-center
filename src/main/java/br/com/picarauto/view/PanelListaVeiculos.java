package br.com.picarauto.view;

/**
 * Lista de veículos — integrada ao backend via VeiculoController.
 * Exibe placa, cor, modelo, marca e cliente proprietário.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.controller.MarcaController;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.MarcaModel;
import br.com.picarauto.model.OrdemServicoModel;
import br.com.picarauto.repository.IHistoricoVeiculoRepository;
import br.com.picarauto.repository.IOrdemServicoRepository;

public class PanelListaVeiculos extends JPanel {

    private final MainFrame frame;

    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;

    private List<VeiculoModel> veiculosAtuais;

    private static final String[] COLUNAS = {"Placa", "Cor", "Modelo", "Marca", "Proprietário", "Editar", "Ver"};

    private static final DateTimeFormatter FMT_DATA     = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final NumberFormat FMT_MOEDA =
        NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public PanelListaVeiculos(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);
        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_LISTA_VEICULOS), BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Lista de Veículos");
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
                g2.setColor(MainFrame.COR_NAVY);
                g2.fillOval(0, 0, 30, 30);
                String car = new String(Character.toChars(0x1F697));
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(car, (30 - fm.stringWidth(car)) / 2,
                    (30 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(30, 30));
        JLabel nome = new JLabel(MainFrame.getUsuarioLogado());
        nome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nome.setForeground(new Color(0xccddff));
        p.add(av);
        p.add(nome);
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
            BorderFactory.createLineBorder(new Color(0xD0C9B8)),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setText("Pesquisar...");
        txtBusca.setForeground(Color.GRAY);
        txtBusca.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if ("Pesquisar...".equals(txtBusca.getText())) {
                    txtBusca.setText("");
                    txtBusca.setForeground(new Color(0x333333));
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBusca.getText().isEmpty()) {
                    txtBusca.setText("Pesquisar...");
                    txtBusca.setForeground(Color.GRAY);
                }
            }
        });
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e)  { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        JButton btnNovo = criarBotaoNavy("Novo veículo", 130, 34);
        btnNovo.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);

        cmbOrdenar = new JComboBox<>(new String[]{"Padrão", "A-Z (Placa)", "Z-A (Placa)"});
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
        if ("A-Z (Placa)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Z-A (Placa)".equals(sel))
            sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
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

        tabela.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new EditarRenderer());
        tabela.getColumnModel().getColumn(6).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(6).setCellRenderer(new VerRenderer());

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (row < 0 || veiculosAtuais == null) return;
                int modelRow = tabela.convertRowIndexToModel(row);
                if (modelRow >= veiculosAtuais.size()) return;
                if (col == 5) abrirFormEdicaoVeiculo(veiculosAtuais.get(modelRow));
                if (col == 6) abrirHistoricoVeiculo(veiculosAtuais.get(modelRow));
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // ── Carregamento da tabela principal ──────────────────────────────────────
    public void carregarVeiculos() {
        modelo.setRowCount(0);
        try {
            VeiculoController vc     = ContextoAplicacao.getBean(VeiculoController.class);
            ClienteController cc     = ContextoAplicacao.getBean(ClienteController.class);
            ModeloController  mc     = ContextoAplicacao.getBean(ModeloController.class);
            MarcaController   mkCtrl = ContextoAplicacao.getBean(MarcaController.class);

            veiculosAtuais           = vc.findAll();
            List<ClienteModel> clientes = cc.findAll();
            List<ModeloModel>  modelos  = mc.findAll();
            List<MarcaModel>   marcas   = mkCtrl.findAll();

            for (VeiculoModel v : veiculosAtuais) {
                String nomeCliente = clientes.stream()
                    .filter(c -> c.getId().equals(v.getIdCliente()))
                    .map(ClienteModel::getNomeCompleto)
                    .findFirst().orElse("-");

                ModeloModel modeloObj = modelos.stream()
                    .filter(m -> m.getId().equals(v.getIdModelo()))
                    .findFirst().orElse(null);

                String nomeModelo = modeloObj != null ? modeloObj.getNomeModelo() : "-";
                String nomeMarca  = modeloObj != null ? marcas.stream()
                    .filter(mk -> mk.getId().equals(modeloObj.getIdMarca()))
                    .map(MarcaModel::getNome)
                    .findFirst().orElse("-") : "-";

                modelo.addRow(new Object[]{
                    formatarPlaca(v.getPlaca()), v.getCor(), nomeModelo, nomeMarca, nomeCliente, ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar veículos: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ── Diálogo de edição/exclusão de veículo ─────────────────────────────────
    private void abrirFormEdicaoVeiculo(VeiculoModel veiculo) {
        VeiculoController vc = ContextoAplicacao.getBean(VeiculoController.class);
        ClienteController cc = ContextoAplicacao.getBean(ClienteController.class);
        ModeloController  mc = ContextoAplicacao.getBean(ModeloController.class);
        MarcaController   mk = ContextoAplicacao.getBean(MarcaController.class);

        // Resolve nomes atuais
        List<ClienteModel> todosClientes;
        List<ModeloModel>  todosModelos;
        List<MarcaModel>   todasMarcas;
        try {
            todosClientes = cc.findAll();
            todosModelos  = mc.findAll();
            todasMarcas   = mk.findAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModeloModel modeloAtual = todosModelos.stream()
            .filter(m -> m.getId().equals(veiculo.getIdModelo()))
            .findFirst().orElse(null);
        String nomeModelo = modeloAtual != null ? modeloAtual.getNomeModelo() : "—";
        String nomeMarca  = modeloAtual != null ? todasMarcas.stream()
            .filter(m -> m.getId().equals(modeloAtual.getIdMarca()))
            .map(MarcaModel::getNome).findFirst().orElse("—") : "—";

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            "Editar Veículo", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);

        // ── Campos ────────────────────────────────────────────────────────────
        JTextField txtPlaca  = criarCampoDialog();
        JTextField txtCor    = criarCampoDialog();
        JTextField txtChassi = criarCampoDialog();
        JComboBox<String> cmbCliente = new JComboBox<>();
        cmbCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbCliente.setBackground(Color.WHITE);
        cmbCliente.setPreferredSize(new Dimension(0, 34));

        // Campos de marca/modelo são somente leitura
        JTextField txtMarca  = criarCampoDialog(); txtMarca.setEditable(false);
        txtMarca.setBackground(new Color(0xEEEEEE));
        JTextField txtModelo = criarCampoDialog(); txtModelo.setEditable(false);
        txtModelo.setBackground(new Color(0xEEEEEE));

        // Preenche os dados atuais (chassi preenchido DEPOIS do PlainDocument)
        txtPlaca.setText(formatarPlaca(veiculo.getPlaca()));
        txtCor.setText(veiculo.getCor());
        txtMarca.setText(nomeMarca);
        txtModelo.setText(nomeModelo);

        // Máscaras — o chassi SÓ recebe setText após o PlainDocument estar instalado
        aplicarMascaraPlacaDialog(txtPlaca);
        aplicarCapitalizacaoDialog(txtCor);
        aplicarLimiteChassiDialog(txtChassi);
        txtChassi.setText(veiculo.getChassi()); // DEPOIS do setDocument

        // Popula combo de clientes
        cmbCliente.addItem("Selecione um cliente...");
        int idxSelecionado = 0;
        for (int i = 0; i < todosClientes.size(); i++) {
            ClienteModel c = todosClientes.get(i);
            cmbCliente.addItem(c.getNomeCompleto() + " — " + c.getTelefone());
            if (c.getId().equals(veiculo.getIdCliente())) idxSelecionado = i + 1;
        }
        final List<ClienteModel> clientesRef = todosClientes;
        cmbCliente.setSelectedIndex(idxSelecionado);

        // ── Layout ────────────────────────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(3, 2, 14, 10));
        grid.setOpaque(false);
        grid.add(criarGrupoDialog("Placa *", txtPlaca));
        grid.add(criarGrupoDialog("Cor *", txtCor));
        grid.add(criarGrupoDialog("Chassi (17 caracteres) *", txtChassi));
        grid.add(criarGrupoComboDialog("Cliente proprietário *", cmbCliente));
        grid.add(criarGrupoDialog("Marca (somente leitura)", txtMarca));
        grid.add(criarGrupoDialog("Modelo (somente leitura)", txtModelo));

        // ── Rodapé ────────────────────────────────────────────────────────────
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);

        JButton btnExcluir = new JButton("Excluir") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.setStroke(new java.awt.BasicStroke(1.5f));
                g2.draw(new java.awt.geom.RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 8, 8));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btnExcluir.setOpaque(false); btnExcluir.setContentAreaFilled(false);
        btnExcluir.setBorderPainted(false); btnExcluir.setFocusPainted(false);
        btnExcluir.setForeground(new Color(0xCC2222));
        btnExcluir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExcluir.setPreferredSize(new Dimension(100, 34));
        btnExcluir.addActionListener(e -> {
            int conf = JOptionPane.showConfirmDialog(dialog,
                "Deseja desativar o veículo " + formatarPlaca(veiculo.getPlaca()) + "?\n"
                + "O registro não será apagado permanentemente.",
                "Confirmar desativação", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    vc.delete(veiculo.getId());
                    dialog.dispose();
                    carregarVeiculos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Erro ao desativar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnCancelar = criarBotaoNavy("Cancelar", 100, 34);
        btnCancelar.addActionListener(e -> dialog.dispose());

        JButton btnSalvar = new JButton("Salvar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btnSalvar.setOpaque(true); btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false); btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalvar.setPreferredSize(new Dimension(100, 34));
        btnSalvar.addActionListener(e -> {
            String placa  = txtPlaca.getText().replace("-", "").toUpperCase().trim();
            String cor    = txtCor.getText().trim();
            String chassi = txtChassi.getText().trim();
            int idxCliente = cmbCliente.getSelectedIndex();

            if (placa.isEmpty() || cor.isEmpty() || chassi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Preencha todos os campos obrigatórios (*).",
                    "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            if (chassi.length() != 17) {
                JOptionPane.showMessageDialog(dialog, "Chassi deve ter exatamente 17 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            if (idxCliente <= 0) {
                JOptionPane.showMessageDialog(dialog, "Selecione um cliente proprietário válido.",
                    "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }

            try {
                veiculo.setPlaca(placa);
                veiculo.setCor(cor);
                veiculo.setChassi(chassi);
                veiculo.setIdCliente(clientesRef.get(idxCliente - 1).getId());
                vc.update(veiculo);
                JOptionPane.showMessageDialog(dialog, "Veículo atualizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarVeiculos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        rodape.add(btnExcluir);
        rodape.add(btnCancelar);
        rodape.add(btnSalvar);

        JPanel form = new JPanel();
        form.setBackground(MainFrame.COR_CREAM);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 20, 24));
        form.add(grid);
        form.add(Box.createVerticalStrut(16));
        form.add(rodape);

        dialog.add(form);
        dialog.setVisible(true);
    }
    
    // ── Diálogo "Histórico do Veículo" ────────────────────────────────────────
    private void abrirHistoricoVeiculo(VeiculoModel v) {

        // Proprietário atual (via idCliente do veículo)
        String proprietarioAtual = "-";
        List<ClienteModel> todosClientes = new ArrayList<>();
        try {
            todosClientes = ContextoAplicacao.getBean(ClienteController.class).findAll();
            proprietarioAtual = todosClientes.stream()
                .filter(c -> c.getId().equals(v.getIdCliente()))
                .map(ClienteModel::getNomeCompleto)
                .findFirst().orElse("-");
        } catch (Exception ignored) {}

        // Histórico de proprietários: começa com o atual + histórico do banco
        List<String> nomesProprietarios = new ArrayList<>();
        nomesProprietarios.add(proprietarioAtual + " (atual)");

        try {
            IHistoricoVeiculoRepository histRepo =
                ContextoAplicacao.getBean(IHistoricoVeiculoRepository.class);

            // ── NOVO: injeta no BD se o par proprietário/veículo ainda não existe ──
            if (!histRepo.existsByIdPessoaAndIdVeiculo(v.getIdCliente(), v.getId())) {
                histRepo.save(v.getIdCliente(), v.getId(), LocalDate.now(), null);
            }

            List<Long> idsPessoa = histRepo.findIdPessoaByIdVeiculo(v.getId());

            final List<ClienteModel> clientesRef = todosClientes;
            for (Long idP : idsPessoa) {
                if (idP.equals(v.getIdCliente())) continue;

                String nome = clientesRef.stream()
                    .filter(c -> c.getId().equals(idP))
                    .map(ClienteModel::getNomeCompleto)
                    .findFirst()
                    .orElse("Pessoa ID " + idP);
                nomesProprietarios.add(nome + " (anterior)");
            }
        } catch (Exception ex) {
            nomesProprietarios.add("(erro ao carregar histórico: " + ex.getMessage() + ")");
        }

        // OS vinculadas ao veículo
        List<OrdemServicoModel> ordens = new ArrayList<>();
        try {
            ordens = ContextoAplicacao.getBean(IOrdemServicoRepository.class)
                         .findAllByIdVeiculo(v.getId());
        } catch (Exception ignored) {}
        final List<OrdemServicoModel> ordensFinais = ordens;

        // ── Monta o JDialog ───────────────────────────────────────────────────
        JDialog dialog = new JDialog(
            SwingUtilities.getWindowAncestor(this),
            "Histórico do Veículo",
            java.awt.Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setSize(620, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblPlaca = new JLabel(formatarPlaca(v.getPlaca()));
        lblPlaca.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPlaca.setForeground(MainFrame.COR_GOLD);

        JLabel lblSub = new JLabel("Histórico completo do veículo");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(0xccddff));

        JPanel headerTexto = new JPanel();
        headerTexto.setOpaque(false);
        headerTexto.setLayout(new BoxLayout(headerTexto, BoxLayout.Y_AXIS));
        headerTexto.add(lblPlaca);
        headerTexto.add(lblSub);
        header.add(headerTexto, BorderLayout.WEST);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(MainFrame.COR_CREAM);
        body.setBorder(new EmptyBorder(16, 20, 16, 20));

        // ── Seção: Dados do veículo ───────────────────────────────────────────
        String dataCadastro = v.getDataHoraCriacao() != null
            ? v.getDataHoraCriacao().format(FMT_DATETIME) : "—";

        body.add(criarTituloSecao("Dados do Veículo"));
        body.add(Box.createVerticalStrut(6));
        body.add(criarLinhaInfo("Placa",             formatarPlaca(v.getPlaca())));
        body.add(criarLinhaInfo("Cor",               v.getCor()));
        body.add(criarLinhaInfo("Chassi",            v.getChassi()));
        body.add(criarLinhaInfo("Cadastrado em",     dataCadastro));
        body.add(criarLinhaInfo("Proprietário atual", proprietarioAtual));
        body.add(Box.createVerticalStrut(14));

        // ── Seção: Histórico de proprietários ─────────────────────────────────
        body.add(criarTituloSecao("Histórico de Proprietários"));
        body.add(Box.createVerticalStrut(6));
        for (int i = 0; i < nomesProprietarios.size(); i++) {
            String label = "Proprietário " + (i + 1);
            body.add(criarLinhaInfo(label, nomesProprietarios.get(i)));
        }
        body.add(Box.createVerticalStrut(14));

        // ── Seção: Ordens de serviço ──────────────────────────────────────────
        body.add(criarTituloSecao("Ordens de Serviço"));
        body.add(Box.createVerticalStrut(6));

        if (ordensFinais.isEmpty()) {
            body.add(criarLinhaVazia("Nenhuma OS vinculada a este veículo."));
        } else {
            for (OrdemServicoModel os : ordensFinais) {
                String dataAb = os.getDataAbertura() != null
                    ? os.getDataAbertura().format(FMT_DATA) : "—";
                String dataFe = os.getDataFechamento() != null
                    ? os.getDataFechamento().format(FMT_DATA) : "Em aberto";
                String valor = os.getValorTotal() != null
                    ? FMT_MOEDA.format(os.getValorTotal()) : "—";

                JPanel rowOS = new JPanel(new BorderLayout(10, 0));
                rowOS.setOpaque(false);
                rowOS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
                rowOS.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel lblOS = new JLabel(
                    "OS #" + os.getId()
                    + "  |  Abertura: " + dataAb
                    + "  |  Fechamento: " + dataFe
                    + "  |  " + valor
                );
                lblOS.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblOS.setForeground(new Color(0x333333));

                JLabel badge = new JLabel(os.getStatus().name());
                badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
                badge.setForeground(Color.WHITE);
                badge.setOpaque(true);
                badge.setBorder(new EmptyBorder(2, 8, 2, 8));
                badge.setBackground(corStatus(os.getStatus()));

                rowOS.add(lblOS,  BorderLayout.CENTER);
                rowOS.add(badge,  BorderLayout.EAST);
                body.add(rowOS);
                body.add(Box.createVerticalStrut(4));
            }
        }

        JScrollPane scrollBody = new JScrollPane(body);
        scrollBody.setBorder(BorderFactory.createEmptyBorder());
        scrollBody.getViewport().setBackground(MainFrame.COR_CREAM);
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        footer.setBackground(MainFrame.COR_CREAM);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, MainFrame.COR_BORDER));
        JButton btnFechar = criarBotaoNavy("Fechar", 100, 34);
        btnFechar.addActionListener(e -> dialog.dispose());
        footer.add(btnFechar);

        dialog.add(header,     BorderLayout.NORTH);
        dialog.add(scrollBody, BorderLayout.CENTER);
        dialog.add(footer,     BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ── Formata placa com máscara visual ─────────────────────────────────────
    private String formatarPlaca(String placa) {
        if (placa == null || placa.length() < 7) return placa != null ? placa : "—";
        String p = placa.toUpperCase().replace("-", "").trim();
        if (p.length() == 7)
            return p.substring(0, 3) + "-" + p.substring(3);
        return placa;
    }

    // ── Helpers visuais do diálogo ────────────────────────────────────────────
    private JPanel criarTituloSecao(String texto) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(MainFrame.COR_NAVY);
        JSeparator sep = new JSeparator();
        sep.setForeground(MainFrame.COR_BORDER);
        p.add(lbl, BorderLayout.WEST);
        p.add(sep, BorderLayout.SOUTH);
        return p;
    }

    private JPanel criarLinhaInfo(String label, String valor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel lblLabel = new JLabel(label + ":  ");
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLabel.setForeground(new Color(0x555555));
        lblLabel.setPreferredSize(new Dimension(160, 20));
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblValor.setForeground(new Color(0x222222));
        row.add(lblLabel);
        row.add(lblValor);
        return row;
    }

    private JPanel criarLinhaVazia(String msg) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl.setForeground(new Color(0x888888));
        row.add(lbl);
        return row;
    }

    private Color corStatus(OrdemServicoModel.StatusOrdemServico status) {
        return switch (status) {
            case ORCAMENTO  -> new Color(0x7B6FAB);
            case EXECUCAO   -> new Color(0x2E7D9E);
            case PAGAMENTO  -> new Color(0xC08A1E);
            case FINALIZADO -> new Color(0x3A7A4A);
        };
    }

    // ── Helpers de botão ──────────────────────────────────────────────────────
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
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }
    
    // ── Helpers de campo do dialog ────────────────────────────────────────────
    private JTextField criarCampoDialog() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34));
        return f;
    }

    private JPanel criarGrupoDialog(String label, JTextField campo) {
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

    private JPanel criarGrupoComboDialog(String label, JComboBox<String> combo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(combo);
        return g;
    }

    // ── Máscaras do dialog ────────────────────────────────────────────────────
    private void aplicarMascaraPlacaDialog(JTextField campo) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;
            private void formatar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    try {
                        String raw = campo.getText().replace("-","").toUpperCase().replaceAll("[^A-Z0-9]","");
                        if (raw.length() > 7) raw = raw.substring(0, 7);
                        boolean mercosul = raw.length() >= 5 && Character.isLetter(raw.charAt(4));
                        String fmt = (!mercosul && raw.length() > 3)
                            ? raw.substring(0,3) + "-" + raw.substring(3)
                            : raw;
                        campo.setText(fmt);
                    } finally { atualizando = false; }
                });
            }
            @Override public void insertUpdate(DocumentEvent e) { formatar(); }
            @Override public void removeUpdate(DocumentEvent e) { formatar(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void aplicarCapitalizacaoDialog(JTextField campo) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;
            private void formatar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    try {
                        String t = campo.getText();
                        if (!t.isEmpty()) {
                            String cap = Character.toUpperCase(t.charAt(0)) + t.substring(1);
                            if (!cap.equals(t)) {
                                int c = campo.getCaretPosition();
                                campo.setText(cap);
                                campo.setCaretPosition(Math.min(c, cap.length()));
                            }
                        }
                    } finally { atualizando = false; }
                });
            }
            @Override public void insertUpdate(DocumentEvent e) { formatar(); }
            @Override public void removeUpdate(DocumentEvent e) {}
            @Override public void changedUpdate(DocumentEvent e) {}
        });
    }

    private void aplicarLimiteChassiDialog(JTextField campo) {
        campo.setDocument(new javax.swing.text.PlainDocument() {
            @Override public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                String f = str.toUpperCase().replaceAll("[^A-Z0-9]", "");
                int espaco = 17 - getLength();
                if (espaco <= 0) return;
                if (f.length() > espaco) f = f.substring(0, espaco);
                super.insertString(offs, f, a);
            }
        });
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
    static class VerRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Ver", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}