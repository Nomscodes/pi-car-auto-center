package br.com.picarauto.view;

/**
 * Tela de rastreabilidade — histórico de Fornecedores e Peças.
 * Exibe os registros do banco em modo somente leitura.
 * Botão "Gerar PDF" preparado para integração futura.
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
import java.util.List;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.FornecedorController;
import br.com.picarauto.controller.PecaController;
import br.com.picarauto.model.FornecedorModel;
import br.com.picarauto.model.PecaModel;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;

import br.com.picarauto.adapter.IRelatorioPdfAdapter;
import br.com.picarauto.adapter.RelatorioPdfAdapter;

public class PanelRastreabilidade extends JPanel {

    private final MainFrame frame;

    private boolean abaFornecedor = true;
    private JButton btnAbaFornecedor, btnAbaPeca;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    private List<FornecedorModel> fornecedores;
    private List<PecaModel>       pecas;

    private static final String[] COLUNAS_FORN = {"Nome / Razão Social", "CNPJ", "Telefone", "E-mail"};
    private static final String[] COLUNAS_PECA = {"Código", "Modelo", "Marca", "Ano Veículo", "Preço Unit.", "Garantia (m)", "Fornecedor"};

    public PanelRastreabilidade(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_RASTREABILIDADE), BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Rastreabilidade");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        bar.add(lbl, BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    private JPanel criarConteudo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MainFrame.COR_CREAM);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.add(criarBarraFerr(), BorderLayout.NORTH);
        p.add(criarScrollTabela(), BorderLayout.CENTER);
        p.add(criarRodape(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel criarBarraFerr() {
        // Abas
        JPanel abas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        abas.setOpaque(false);
        btnAbaFornecedor = criarBotaoAba("Fornecedores");
        btnAbaPeca       = criarBotaoAba("Peças");
        btnAbaFornecedor.addActionListener(e -> { abaFornecedor = true;  carregarDados(); btnAbaFornecedor.repaint(); btnAbaPeca.repaint(); });
        btnAbaPeca.addActionListener(e ->       { abaFornecedor = false; carregarDados(); btnAbaFornecedor.repaint(); btnAbaPeca.repaint(); });
        abas.add(btnAbaFornecedor);
        abas.add(btnAbaPeca);

        // Busca
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
            @Override public void insertUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e)  { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
            void filtrar() {
                if (sorter == null) return;
                String txt = txtBusca.getText().trim();
                sorter.setRowFilter("Pesquisar...".equals(txt) || txt.isEmpty() ? null : RowFilter.regexFilter("(?i)" + txt));
            }
        });

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);

        JPanel barra = new JPanel(new BorderLayout(0, 10));
        barra.setOpaque(false);
        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        barra.add(abas, BorderLayout.NORTH);
        barra.add(painelBusca, BorderLayout.SOUTH);
        return barra;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS_FORN, 0) {
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

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblInfo = new JLabel("Dados exibidos em modo somente leitura — rastreabilidade de fornecedores e peças.");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(0x888888));

        // Botão Gerar PDF — preparado para integração futura com Apache POI ou iText
        JButton btnPDF = new JButton("Gerar PDF") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GREEN.brighter() : MainFrame.COR_GREEN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnPDF.setOpaque(true);
        btnPDF.setContentAreaFilled(false);
        btnPDF.setBorderPainted(false);
        btnPDF.setFocusPainted(false);
        btnPDF.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPDF.setPreferredSize(new Dimension(120, 36));
        // integrado via RelatorioPdfAdapter
        btnPDF.addActionListener(e -> gerarPdf());

        rodape.add(lblInfo, BorderLayout.WEST);
        rodape.add(btnPDF,  BorderLayout.EAST);
        return rodape;
    }

    // Carrega fornecedores ou peças do banco conforme a aba ativa.
    // Chamado pelo MainFrame ao navegar para TELA_RASTREABILIDADE.
    public void carregarDados() {
        modelo.setRowCount(0);

        // Redefine as colunas conforme a aba selecionada
        String[] colunas = abaFornecedor ? COLUNAS_FORN : COLUNAS_PECA;
        modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela.setModel(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        try {
            if (abaFornecedor) {
                // Carrega todos os fornecedores ativos do banco
                FornecedorController fc = ContextoAplicacao.getBean(FornecedorController.class);
                fornecedores = fc.findAll();
                for (FornecedorModel f : fornecedores) {
                    modelo.addRow(new Object[]{
                        f.getNomeFornecedor(),
                        f.getCnpj() != null ? f.getCnpj() : "-",
                        f.getTelefone(),
                        f.getEmail()
                    });
                }
            } else {
                // Carrega todas as peças ativas do banco com nome do fornecedor vinculado
                PecaController pc = ContextoAplicacao.getBean(PecaController.class);
                FornecedorController fc = ContextoAplicacao.getBean(FornecedorController.class);
                pecas = pc.findAll();
                List<FornecedorModel> todosForns = fc.findAll();

                for (PecaModel p : pecas) {
                    // Resolve o nome do fornecedor pelo id para exibição na tabela
                    String nomeForn = todosForns.stream()
                        .filter(f -> f.getId().equals(p.getIdFornecedor()))
                        .map(FornecedorModel::getNomeFornecedor)
                        .findFirst()
                        .orElse("-");
                    modelo.addRow(new Object[]{
                        p.getCodigoNacional(),
                        p.getModelo(),
                        p.getMarca(),
                        p.getAnoVeiculo(),
                        String.format("R$ %.2f", p.getPrecoUnitario()),
                        p.getGarantia() + " m",
                        nomeForn
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gerarPdf() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há dados para gerar o relatório.",
                "Sem dados", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] colunas = abaFornecedor ? COLUNAS_FORN : COLUNAS_PECA;
        List<String[]> linhas = new ArrayList<>();
        for (int linha = 0; linha < modelo.getRowCount(); linha++) {
            String[] valores = new String[colunas.length];
            for (int coluna = 0; coluna < colunas.length; coluna++) {
                Object valor = modelo.getValueAt(linha, coluna);
                valores[coluna] = valor != null ? valor.toString() : "-";
            }
            linhas.add(valores);
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar relatório de " + (abaFornecedor ? "Fornecedores" : "Peças"));
        chooser.setFileFilter(new FileNameExtensionFilter("Documento PDF (*.pdf)", "pdf"));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setSelectedFile(new File(abaFornecedor ? "relatorio_fornecedores.pdf" : "relatorio_pecas.pdf"));
        int opcao = chooser.showSaveDialog(this);
        if (opcao != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".pdf")) {
            arquivo = new File(arquivo.getParentFile(), arquivo.getName() + ".pdf");
        }

        try {
            String titulo = "AV CAR AUTO CENTER — Relatório de " + (abaFornecedor ? "Fornecedores" : "Peças");
            IRelatorioPdfAdapter adapter = new RelatorioPdfAdapter();
            File gerado = adapter.gerar(titulo, colunas, linhas, arquivo);

            JOptionPane.showMessageDialog(this,
                "Relatório gerado com sucesso em:\n" + gerado.getAbsolutePath(),
                "PDF gerado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao gerar o PDF:\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JButton criarBotaoAba(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = ("Fornecedores".equals(texto) && abaFornecedor) || ("Peças".equals(texto) && !abaFornecedor);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER);
                g2.setStroke(new java.awt.BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(ativo ? Color.WHITE : MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 34));
        return btn;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setFont(MainFrame.FONT_SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
