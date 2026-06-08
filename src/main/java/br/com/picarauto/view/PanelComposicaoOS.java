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
import java.util.ArrayList;
import java.util.List;

public class PanelComposicaoOS extends JPanel {

    private final MainFrame frame;

    private JLabel lblTotal;
    private JPanel painelItens;
    private double valorTotal = 345.00;

    private static final Object[][] ITENS_INICIAIS = {
        {"Troca de óleo",                "Serviço interno", "1x",  120.00},
        {"Filtro de óleo",               "Peça",            "1x",   45.00},
        {"Alinhamento e balanceamento",  "Serviço externo", "1x",  180.00},
    };

    public PanelComposicaoOS(MainFrame frame) {
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

        JLabel lblTitulo = new JLabel("Composição da OS #0043");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(MainFrame.COR_GOLD);

        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_MARCA));

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnVoltar, BorderLayout.EAST);
        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 0));
        corpo.setOpaque(false);

        corpo.add(criarCamposOS(), BorderLayout.NORTH);
        corpo.add(criarSecaoItens(), BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarCamposOS() {
        JPanel campos = new JPanel(new GridLayout(2, 2, 14, 10));
        campos.setOpaque(false);
        campos.setBorder(new EmptyBorder(16, 20, 8, 20));

        campos.add(criarCampo("Cliente",                 "João Silva"));
        campos.add(criarCampo("Veículo",                 "Chevrolet Onix 2022 — ABC-1234"));
        campos.add(criarCampo("Colaborador responsável", "Marcos Pereira"));
        campos.add(criarCampo("Data de abertura",        "08/06/2026"));

        return campos;
    }

    private JPanel criarCampo(String label, String valor) {
        JPanel campo = new JPanel();
        campo.setOpaque(false);
        campo.setLayout(new BoxLayout(campo, BoxLayout.Y_AXIS));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLabel.setForeground(new Color(0x555555));

        JPanel valorPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(new Color(0xd0cbc0));
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 6, 6));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        valorPanel.setOpaque(false);
        valorPanel.setBorder(new EmptyBorder(7, 10, 7, 10));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(MainFrame.FONT_NORMAL);
        lblValor.setForeground(new Color(0x222222));
        valorPanel.add(lblValor, BorderLayout.CENTER);

        campo.add(lblLabel);
        campo.add(Box.createVerticalStrut(4));
        campo.add(valorPanel);
        return campo;
    }

    private JPanel criarSecaoItens() {
        JPanel secao = new JPanel(new BorderLayout());
        secao.setOpaque(false);
        secao.setBorder(new EmptyBorder(8, 20, 8, 20));

        JPanel cardItens = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(new Color(0xd0cbc0));
                g2.setStroke(new BasicStroke(0.5f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cardItens.setOpaque(false);

        JPanel headerItens = new JPanel(new BorderLayout());
        headerItens.setBackground(MainFrame.COR_NAVY);
        headerItens.setBorder(new EmptyBorder(8, 14, 8, 14));

        JLabel lblItens = new JLabel("Itens da OS");
        lblItens.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblItens.setForeground(MainFrame.COR_GOLD);

        JButton btnAdicionar = new JButton("+ Adicionar item") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MainFrame.COR_GOLD.darker() : MainFrame.COR_GOLD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 5, 5));
                g2.setColor(MainFrame.COR_NAVY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnAdicionar.setPreferredSize(new Dimension(130, 28));
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setContentAreaFilled(false);
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        headerItens.add(lblItens,     BorderLayout.WEST);
        headerItens.add(btnAdicionar, BorderLayout.EAST);

        painelItens = new JPanel();
        painelItens.setOpaque(false);
        painelItens.setLayout(new BoxLayout(painelItens, BoxLayout.Y_AXIS));

        for (Object[] item : ITENS_INICIAIS) {
            painelItens.add(criarLinhaItem(
                (String) item[0],
                (String) item[1],
                (String) item[2],
                (Double) item[3]
            ));
        }

        cardItens.add(headerItens, BorderLayout.NORTH);
        cardItens.add(painelItens, BorderLayout.CENTER);

        secao.add(cardItens, BorderLayout.CENTER);
        return secao;
    }

    private JPanel criarLinhaItem(String nome, String tipo, String qtd, double valor) {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setOpaque(false);
        linha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xeeeeee)),
            new EmptyBorder(10, 14, 10, 14)
        ));

        JPanel esq = new JPanel();
        esq.setOpaque(false);
        esq.setLayout(new BoxLayout(esq, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNome.setForeground(new Color(0x222222));

        JLabel lblTipo = new JLabel(tipo + " · " + qtd);
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblTipo.setForeground(new Color(0x888888));

        esq.add(lblNome);
        esq.add(Box.createVerticalStrut(2));
        esq.add(lblTipo);

        JLabel lblValor = new JLabel(String.format("R$ %.2f", valor).replace(".", ","));
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblValor.setForeground(MainFrame.COR_NAVY);

        linha.add(esq,      BorderLayout.WEST);
        linha.add(lblValor, BorderLayout.EAST);
        return linha;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);
        rodape.setBorder(new EmptyBorder(0, 20, 16, 20));

        JPanel totalBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        totalBar.setOpaque(false);
        totalBar.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblTotalLabel = new JLabel("Total da OS");
        lblTotalLabel.setFont(MainFrame.FONT_NORMAL);
        lblTotalLabel.setForeground(MainFrame.COR_MUTED);

        lblTotal = new JLabel(String.format("R$ %.2f", valorTotal).replace(".", ","));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(MainFrame.COR_GOLD);

        totalBar.add(lblTotalLabel, BorderLayout.WEST);
        totalBar.add(lblTotal,      BorderLayout.EAST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        acoes.setOpaque(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(MainFrame.FONT_NORMAL);
        btnCancelar.setForeground(new Color(0x666666));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(0xbbbbbb), 1));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(100, 34));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        JButton btnSalvar = new JButton("Abrir como orçamento") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x3a5c4e) : MainFrame.COR_GREEN);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.setColor(MainFrame.COR_CREAM);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btnSalvar.setPreferredSize(new Dimension(200, 34));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_OS));

        acoes.add(btnCancelar);
        acoes.add(btnSalvar);

        rodape.add(totalBar, BorderLayout.CENTER);
        rodape.add(acoes,    BorderLayout.SOUTH);
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
