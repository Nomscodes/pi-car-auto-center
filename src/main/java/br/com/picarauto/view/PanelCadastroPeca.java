package br.com.picarauto.view;

/**
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class PanelCadastroPeca extends JPanel {

    private final MainFrame frame;

    private JTextField txtCodigoNacional;
    private JTextField txtModelo;
    private JTextField txtMarca;
    private JTextField txtAnoVeiculo;
    private JTextField txtAnoModelo;
    private JTextField txtPrecoUnitario;
    private JTextField txtGarantia;
    private JComboBox<String> cmbFornecedor;

    private static final String[] FORNECEDORES = {
        "Selecione o fornecedor...",
        "AutoPeças BR",
        "Distribuidora Goiás",
        "Peças e Cia",
        "Nacional Parts",
        "Moto Peças Center"
    };

    public PanelCadastroPeca(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarHeader(),  BorderLayout.NORTH);
        add(criarCorpo(),   BorderLayout.CENTER);
        add(criarRodape(),  BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainFrame.COR_NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel("Cadastro de peça");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JScrollPane criarCorpo() {
        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(new EmptyBorder(16, 20, 16, 20));

        corpo.add(criarBannerImportacao());
        corpo.add(Box.createVerticalStrut(20));

        corpo.add(criarLabel("Identificação da peça"));
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(criarGrid2(
            criarGrupo("Código nacional", txtCodigoNacional = criarInput("Ex: 12345678")),
            criarGrupo("Preço unitário (R$)", txtPrecoUnitario = criarInput("Ex: 89.90"))
        ));

        corpo.add(Box.createVerticalStrut(14));
        corpo.add(criarLabel("Compatibilidade do veículo"));
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(criarGrid2(
            criarGrupo("Marca do veículo", txtMarca = criarInput("Ex: Chevrolet")),
            criarGrupo("Modelo do veículo", txtModelo = criarInput("Ex: Onix"))
        ));
        corpo.add(Box.createVerticalStrut(10));
        corpo.add(criarGrid2(
            criarGrupo("Ano do veículo", txtAnoVeiculo = criarInput("Ex: 2022")),
            criarGrupo("Ano do modelo", txtAnoModelo = criarInput("Ex: 2022"))
        ));

        corpo.add(Box.createVerticalStrut(14));
        corpo.add(criarLabel("Garantia e fornecedor"));
        corpo.add(Box.createVerticalStrut(8));
        corpo.add(criarGrid2(
            criarGrupo("Garantia (meses)", txtGarantia = criarInput("Ex: 12")),
            criarGrupoCombo("Fornecedor", cmbFornecedor = criarCombo(FORNECEDORES))
        ));

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarBannerImportacao() {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x2d4a3e));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(14, 16, 14, 16));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Importação em lote via Excel");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(MainFrame.COR_CREAM);

        JLabel lblDesc = new JLabel("Importe várias peças de uma vez a partir de um arquivo .xlsx");
        lblDesc.setFont(MainFrame.FONT_SMALL);
        lblDesc.setForeground(new Color(0x9FE1CB));

        texto.add(lblTitulo);
        texto.add(Box.createVerticalStrut(3));
        texto.add(lblDesc);

        JButton btnImportar = new JButton("Importar .xlsx") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnImportar.setPreferredSize(new Dimension(130, 34));
        btnImportar.setBorderPainted(false);
        btnImportar.setContentAreaFilled(false);
        btnImportar.setFocusPainted(false);
        btnImportar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Selecionar arquivo Excel");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Planilhas Excel (*.xlsx)", "xlsx"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "Arquivo selecionado: " + fc.getSelectedFile().getName() + "\n(Adapter será conectado pelo time de backend)",
                    "Importação", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        banner.add(texto,       BorderLayout.WEST);
        banner.add(btnImportar, BorderLayout.EAST);
        return banner;
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel criarGrid2(JPanel a, JPanel b) {
        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.add(a);
        grid.add(b);
        return grid;
    }

    private JPanel criarGrupo(String label, JTextField campo) {
        JPanel grupo = new JPanel();
        grupo.setOpaque(false);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(4));
        grupo.add(campo);
        return grupo;
    }

    private JPanel criarGrupoCombo(String label, JComboBox<String> combo) {
        JPanel grupo = new JPanel();
        grupo.setOpaque(false);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(0x555555));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        grupo.add(lbl);
        grupo.add(Box.createVerticalStrut(4));
        grupo.add(combo);
        return grupo;
    }

    private JTextField criarInput(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(0xaaaaaa));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10,
                        getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(MainFrame.FONT_NORMAL);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xd0cbc0), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(0, 34));
        return field;
    }

    private JComboBox<String> criarCombo(String[] itens) {
        JComboBox<String> combo = new JComboBox<>(itens);
        combo.setFont(MainFrame.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(0, 34));
        return combo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        rodape.setBackground(MainFrame.COR_CREAM_ALT);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xd0cbc0)));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(MainFrame.FONT_NORMAL);
        btnCancelar.setForeground(new Color(0x666666));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(0xbbbbbb), 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(100, 34));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_DASHBOARD));

        JButton btnSalvar = new JButton("Salvar peça") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x223060) : MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(MainFrame.COR_GOLD);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnSalvar.setPreferredSize(new Dimension(130, 34));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
    }

    private JButton criarBotaoVoltar() {
        JButton btn = new JButton("← Voltar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1e3060));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
                g2.setFont(MainFrame.FONT_SMALL);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD : MainFrame.COR_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
