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
import java.util.List;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.controller.ModeloController;
import br.com.picarauto.controller.MarcaController;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.ModeloModel;
import br.com.picarauto.model.MarcaModel;

public class PanelListaVeiculos extends JPanel {

    private final MainFrame frame;

    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbOrdenar;

    private List<VeiculoModel> veiculosAtuais;

    private static final String[] COLUNAS = {"Placa", "Cor", "Modelo", "Marca", "Proprietário", ""};

    public PanelListaVeiculos(MainFrame frame) {
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
        if ("A-Z (Placa)".equals(sel)) sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        else if ("Z-A (Placa)".equals(sel)) sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
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

        tabela.getColumnModel().getColumn(5).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(5).setCellRenderer(new EditarRenderer());

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (col == 5 && row >= 0 && veiculosAtuais != null) {
                    int modelRow = tabela.convertRowIndexToModel(row);
                    if (modelRow < veiculosAtuais.size()) {
                        VeiculoModel v = veiculosAtuais.get(modelRow);
                        JOptionPane.showMessageDialog(
                            PanelListaVeiculos.this,
                            "Placa: " + v.getPlaca() + "\nCor: " + v.getCor() + "\nChassi: " + v.getChassi(),
                            "Detalhes do Veículo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public void carregarVeiculos() {
        modelo.setRowCount(0);
        try {
            VeiculoController vc     = ContextoAplicacao.getBean(VeiculoController.class);
            ClienteController cc     = ContextoAplicacao.getBean(ClienteController.class);
            ModeloController  mc     = ContextoAplicacao.getBean(ModeloController.class);
            MarcaController   mkCtrl = ContextoAplicacao.getBean(MarcaController.class);

            veiculosAtuais          = vc.findAll();
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

                modelo.addRow(new Object[]{v.getPlaca(), v.getCor(), nomeModelo, nomeMarca, nomeCliente, ""});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar veículos: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
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
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(true); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    static class EditarRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            JLabel lbl = new JLabel("Ver", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(MainFrame.COR_NAVY);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            return lbl;
        }
    }
}
