package br.com.picarauto.view;

/**
 * Cadastro de serviços — abas Internos / Externos integradas ao backend.
 *
 * @author Cassiano
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;

import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.ServicoInternoController;
import br.com.picarauto.controller.ServicoExternoController;
import br.com.picarauto.model.ServicoInternoModel;
import br.com.picarauto.model.ServicoExternoModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;

public class PanelCadastroServicos extends JPanel {

    private final MainFrame frame;

    private boolean abaInterno = true;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnAbaInternos, btnAbaExternos;

    private List<ServicoInternoModel> servicosInternos;
    private List<ServicoExternoModel> servicosExternos;

    private static final String[] COLUNAS = {"Descrição", "Tipo", "Valor Cobrado", ""};

    public PanelCadastroServicos(MainFrame frame) {
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
        inner.add(new SidebarPanel(frame, MainFrame.TELA_SERVICOS), BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
    }

    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));
        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Serviços");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14)); lbl.setForeground(Color.WHITE);
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
        p.add(av); p.add(nome); return p;
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
        btnAbaInternos = criarBotaoAba("Internos");
        btnAbaExternos = criarBotaoAba("Externos");
        btnAbaInternos.addActionListener(e -> { abaInterno = true;  if (br.com.picarauto.util.ContextoAplicacao.isReady()) carregarServicos(); btnAbaInternos.repaint(); btnAbaExternos.repaint(); });
        btnAbaExternos.addActionListener(e -> { abaInterno = false; if (br.com.picarauto.util.ContextoAplicacao.isReady()) carregarServicos(); btnAbaInternos.repaint(); btnAbaExternos.repaint(); });
        abas.add(btnAbaInternos); abas.add(btnAbaExternos);

        txtBusca = new JTextField();
        txtBusca.setPreferredSize(new Dimension(0, 36));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xD0C9B8)), BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBackground(Color.WHITE);
        txtBusca.setText("Pesquisar..."); txtBusca.setForeground(Color.GRAY);
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

        JButton btnNovo = criarBotaoNavy("Novo serviço", 130, 34);
        btnNovo.addActionListener(e -> abrirFormServico(null, null));

        JPanel painelBusca = new JPanel(new BorderLayout(12, 0));
        painelBusca.setBackground(new Color(0xF5F0E6));
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnNovo, BorderLayout.EAST);

        JPanel meio = new JPanel(new BorderLayout(0, 6));
        meio.setOpaque(false);
        meio.add(painelBusca, BorderLayout.CENTER);

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
                boolean ativo = ("Internos".equals(texto) && abaInterno) || ("Externos".equals(texto) && !abaInterno);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER); g2.setStroke(new java.awt.BasicStroke(1f));
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
        btn.setPreferredSize(new Dimension(110, 34)); return btn;
    }

    private JScrollPane criarScrollTabela() {
        modelo = new DefaultTableModel(COLUNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);
        tabela.setFont(MainFrame.FONT_NORMAL); tabela.setRowHeight(32);
        tabela.setShowGrid(false); tabela.setIntercellSpacing(new Dimension(12, 0));
        tabela.setBackground(Color.WHITE);
        tabela.setSelectionBackground(new Color(0xe8e3d8)); tabela.setSelectionForeground(MainFrame.COR_NAVY);
        tabela.setFillsViewportHeight(true); tabela.setDefaultEditor(Object.class, null);

        JTableHeader header = tabela.getTableHeader();
        header.setBackground(MainFrame.COR_CREAM_ALT); header.setForeground(new Color(0x444444));
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, MainFrame.COR_BORDER));
        header.setReorderingAllowed(false);

        tabela.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setCellRenderer(new TipoPillRenderer());
        tabela.getColumnModel().getColumn(3).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(3).setCellRenderer(new EditarRenderer());

        tabela.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int col = tabela.columnAtPoint(e.getPoint());
                int row = tabela.rowAtPoint(e.getPoint());
                if (col != 3 || row < 0) return;
                int modelRow = tabela.convertRowIndexToModel(row);
                if (abaInterno && servicosInternos != null && modelRow < servicosInternos.size())
                    abrirFormServico(servicosInternos.get(modelRow), null);
                else if (!abaInterno && servicosExternos != null && modelRow < servicosExternos.size())
                    abrirFormServico(null, servicosExternos.get(modelRow));
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public void carregarServicos() {
        modelo.setRowCount(0);
        try {
            if (abaInterno) {
                ServicoInternoController sic = ContextoAplicacao.getBean(ServicoInternoController.class);
                servicosInternos = sic.findAll();
                for (ServicoInternoModel s : servicosInternos)
                    modelo.addRow(new Object[]{ s.getDescricao(), "Interno", String.format("R$ %.2f", s.getValorCobrado()), "" });
            } else {
                ServicoExternoController sec = ContextoAplicacao.getBean(ServicoExternoController.class);
                servicosExternos = sec.findAll();
                for (ServicoExternoModel s : servicosExternos)
                    modelo.addRow(new Object[]{ s.getDescricao(), "Externo", String.format("R$ %.2f", s.getValorCobrado()), "" });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormServico(ServicoInternoModel interno, ServicoExternoModel externo) {
        boolean editando = interno != null || externo != null;
        boolean isInterno = interno != null || (externo == null && abaInterno);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
            editando ? "Editar Serviço" : "Novo Serviço",
            java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 280);
        dialog.setLocationRelativeTo(this);

        boolean[] modoExterno = { !isInterno };
        JButton btnInt = criarBotaoToggle("Interno", modoExterno);
        JButton btnExt = criarBotaoToggle("Externo", modoExterno);
        btnInt.addActionListener(e -> { modoExterno[0] = false; btnInt.repaint(); btnExt.repaint(); });
        btnExt.addActionListener(e -> { modoExterno[0] = true;  btnInt.repaint(); btnExt.repaint(); });
        if (editando) { btnInt.setEnabled(false); btnExt.setEnabled(false); }

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false); togglePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        togglePanel.add(btnInt); togglePanel.add(btnExt);

        JTextField txtDescricao = criarCampo();
        JTextField txtValor     = criarCampo();

        if (interno != null)  { txtDescricao.setText(interno.getDescricao());  txtValor.setText(String.valueOf(interno.getValorCobrado())); }
        if (externo != null)  { txtDescricao.setText(externo.getDescricao());  txtValor.setText(String.valueOf(externo.getValorCobrado())); }

        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false); grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        grid.add(criarGrupo("Descrição *", txtDescricao));
        grid.add(criarGrupo("Valor cobrado (R$) *", txtValor));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false); rodape.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (editando) {
            JButton btnExcluir = criarBotaoOutline("Excluir", 100, 34);
            btnExcluir.setForeground(new Color(0xCC2222));
            btnExcluir.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(dialog, "Deseja excluir este serviço?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        if (interno != null) ContextoAplicacao.getBean(ServicoInternoController.class).delete(interno.getId());
                        else                 ContextoAplicacao.getBean(ServicoExternoController.class).delete(externo.getId());
                        dialog.dispose(); carregarServicos();
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
            String desc = txtDescricao.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Preencha a descrição.", "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            double valor;
            try { valor = Double.parseDouble(txtValor.getText().trim().replace(",", ".")); }
            catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Valor inválido.", "Atenção", JOptionPane.WARNING_MESSAGE); return;
            }
            try {
                if (!modoExterno[0]) {
                    ServicoInternoController sic = ContextoAplicacao.getBean(ServicoInternoController.class);
                    ServicoInternoModel s = interno != null ? interno : new ServicoInternoModel();
                    s.setDescricao(desc); s.setValorCobrado(valor);
                    if (editando) sic.update(s); else sic.insert(s);
                } else {
                    ServicoExternoController sec = ContextoAplicacao.getBean(ServicoExternoController.class);
                    ServicoExternoModel s = externo != null ? externo : new ServicoExternoModel();
                    s.setDescricao(desc); s.setValorCobrado(valor);
                    if (editando) sec.update(s); else sec.insert(s);
                }
                // Muda a aba para o tipo que acabou de salvar
                abaInterno = !modoExterno[0];
                btnAbaInternos.repaint(); btnAbaExternos.repaint();
                dialog.dispose(); carregarServicos();
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
        form.add(togglePanel); form.add(Box.createVerticalStrut(14));
        form.add(grid); form.add(Box.createVerticalStrut(14)); form.add(rodape);
        dialog.add(form);
        dialog.setVisible(true);
    }

    private JButton criarBotaoToggle(String texto, boolean[] modoExterno) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean ativo = ("Interno".equals(texto) && !modoExterno[0]) || ("Externo".equals(texto) && modoExterno[0]);
                g2.setColor(ativo ? MainFrame.COR_NAVY : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(MainFrame.COR_BORDER); g2.setStroke(new java.awt.BasicStroke(1f));
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
        btn.setPreferredSize(new Dimension(110, 34)); return btn;
    }

    private JPanel criarGrupo(String label, JTextField campo) {
        JPanel g = new JPanel(); g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11)); lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        g.add(lbl); g.add(Box.createVerticalStrut(4)); g.add(campo); return g;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField(); f.setFont(MainFrame.FONT_NORMAL); f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MainFrame.COR_BORDER, 1), new EmptyBorder(6, 10, 6, 10)));
        f.setPreferredSize(new Dimension(0, 34)); return f;
    }

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

    static class TipoPillRenderer extends DefaultTableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            String tipo = v == null ? "" : v.toString();
            boolean interno = "Interno".equals(tipo);
            JLabel lbl = new JLabel(tipo, SwingConstants.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(interno ? new Color(0xE6F1FB) : new Color(0xFAEEDA));
                    g2.fillRoundRect(4, (getHeight() - 20) / 2, getWidth() - 8, 20, 10, 10);
                    g2.dispose(); super.paintComponent(g);
                }
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(interno ? new Color(0x185FA5) : new Color(0x854F0B));
            lbl.setOpaque(false); return lbl;
        }
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
