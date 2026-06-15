package br.com.picarauto.view;

/**
 * Formulário de cadastro de veículo — campos em grid 2 colunas.
 * Marca e modelo são readonly (vindos da seleção anterior).
 *
 * @author Cassiano / melhorias por Caio4breu
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

// Imports do backend
import br.com.picarauto.util.ContextoAplicacao;
import br.com.picarauto.controller.VeiculoController;
import br.com.picarauto.controller.ClienteController;
import br.com.picarauto.model.VeiculoModel;
import br.com.picarauto.model.ClienteModel;
import br.com.picarauto.model.exception.FieldValidationException;
import br.com.picarauto.model.exception.RuleValidationException;

public class PanelCadastroVeiculo extends JPanel {

    private final MainFrame frame;

    // Campos do formulário
    private JTextField txtPlaca, txtCor, txtChassi;
    private JComboBox<String> cmbMarca, cmbModelo, cmbCliente;

    // Flag para evitar loop recursivo na máscara de placa
    private boolean atualizandoPlaca = false;

    // Lista de clientes carregada uma vez ao montar a tela
    private List<ClienteModel> listaClientes;

    public PanelCadastroVeiculo(MainFrame frame) {
        this.frame = frame;
        setBackground(MainFrame.COR_CREAM);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        add(criarTopbar(), BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(MainFrame.COR_CREAM);
        inner.add(criarScrollConteudo(), BorderLayout.CENTER);
        inner.add(new SidebarPanel(frame, MainFrame.TELA_VEICULO), BorderLayout.EAST);

        add(inner, BorderLayout.CENTER);
    }

    // ── Topbar ────────────────────────────────────────────────────────────────
    private JPanel criarTopbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MainFrame.COR_NAVY);
        bar.setPreferredSize(new Dimension(0, 48));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lbl = new JLabel("AV CAR AUTO CENTER  —  Cadastro de Veículo");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);

        // BOTÃO TELA CADASTRO VEÍCULO DE VOLTAR PARA LISTA DE CLIENTES
        JButton btnVoltar = criarBotaoVoltar();
        btnVoltar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_CLIENTES));

        bar.add(lbl,       BorderLayout.WEST);
        bar.add(btnVoltar, BorderLayout.EAST);
        return bar;
    }

    // ── Conteúdo ──────────────────────────────────────────────────────────────
    private JScrollPane criarScrollConteudo() {
        JPanel corpo = new JPanel();
        corpo.setBackground(MainFrame.COR_CREAM);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(new EmptyBorder(24, 24, 24, 24));

        corpo.add(criarLabelSecao("Identificação do veículo"));
        corpo.add(Box.createVerticalStrut(10));

        txtPlaca  = criarCampo();
        txtCor    = criarCampo();
        txtChassi = criarCampo();
        cmbCliente = criarCombo(new String[]{"Carregando clientes..."});

        aplicarMascaraPlaca();
        aplicarCapitalizacaoCor();
        aplicarLimiteChassi();
        carregarClientes();

        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(criarGrupo("Placa (padrão antigo ABC-1234 ou Mercosul ABC1D23) *", txtPlaca));
        grid.add(criarGrupo("Cor *", txtCor));
        grid.add(criarGrupo("Chassi (exatamente 17 caracteres) *", txtChassi));
        grid.add(criarGrupoCombo("Cliente proprietário *", cmbCliente));

        corpo.add(grid);
        corpo.add(Box.createVerticalStrut(20));

        corpo.add(criarLabelSecao("Marca e modelo"));
        corpo.add(Box.createVerticalStrut(10));
        corpo.add(criarGridMarcaModelo());

        corpo.add(Box.createVerticalStrut(24));
        corpo.add(criarRodapeAcoes());

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    // ── Máscara de placa ──────────────────────────────────────────────────────
    /**
     * Aplica máscara visual à placa enquanto o usuário digita:
     * - Padrão antigo:  ABC-1234  (letras-números)
     * - Mercosul:       ABC1D23   (sem hífen, letra na 5ª posição)
     *
     * O hífen é inserido/removido automaticamente. O valor salvo no banco
     * é sempre a versão normalizada sem hífen e em maiúsculas.
     */
    private void aplicarMascaraPlaca() {
        txtPlaca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { formatar(); }
            @Override public void removeUpdate(DocumentEvent e)  { formatar(); }
            @Override public void changedUpdate(DocumentEvent e) { formatar(); }

            private void formatar() {
                if (atualizandoPlaca) return;
                atualizandoPlaca = true;

                SwingUtilities.invokeLater(() -> {
                    try {
                        // 1. Pega o texto cru, remove hífen e converte para maiúsculas
                        String raw = txtPlaca.getText()
                                .replace("-", "")
                                .toUpperCase()
                                .replaceAll("[^A-Z0-9]", "");

                        // Limita ao máximo de 7 caracteres úteis
                        if (raw.length() > 7) raw = raw.substring(0, 7);

                        // 2. Detecta se é Mercosul (posição 4, índice 3, é letra)
                        //    ABC1D23 → a 5ª posição (idx 4) é letra
                        boolean isMercosul = raw.length() >= 5
                                && Character.isLetter(raw.charAt(4));

                        // 3. Monta a string formatada
                        String formatado;
                        if (!isMercosul && raw.length() > 3) {
                            // Padrão antigo: ABC-XXXX
                            formatado = raw.substring(0, 3) + "-" + raw.substring(3);
                        } else {
                            // Mercosul ou ainda incompleto: sem hífen
                            formatado = raw;
                        }

                        txtPlaca.setText(formatado);

                    } finally {
                        atualizandoPlaca = false;
                    }
                });
            }
        });
    }

    // ── Capitalização da cor ──────────────────────────────────────────────────
    private void aplicarCapitalizacaoCor() {
        txtCor.getDocument().addDocumentListener(new DocumentListener() {
            private boolean atualizando = false;

            @Override public void insertUpdate(DocumentEvent e)  { capitalizar(); }
            @Override public void removeUpdate(DocumentEvent e)  {}
            @Override public void changedUpdate(DocumentEvent e) {}

            private void capitalizar() {
                if (atualizando) return;
                atualizando = true;
                SwingUtilities.invokeLater(() -> {
                    try {
                        String texto = txtCor.getText();
                        if (!texto.isEmpty()) {
                            String capitalizado = Character.toUpperCase(texto.charAt(0))
                                    + texto.substring(1);
                            if (!capitalizado.equals(texto)) {
                                int caret = txtCor.getCaretPosition();
                                txtCor.setText(capitalizado);
                                // Reposiciona o cursor para não pular para o fim
                                txtCor.setCaretPosition(Math.min(caret, capitalizado.length()));
                            }
                        }
                    } finally {
                        atualizando = false;
                    }
                });
            }
        });
    }

    // ── Limite de 17 caracteres no chassi ────────────────────────────────────
    /**
     * Usa PlainDocument customizado para bloquear entrada além de 17 chars
     * e forçar maiúsculas imediatamente, sem precisar de DocumentListener.
     */
    private void aplicarLimiteChassi() {
        txtChassi.setDocument(new PlainDocument() {
            private static final int LIMITE = 17;

            @Override
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (str == null) return;
                // Aceita só alfanuméricos e força maiúsculas
                String filtrado = str.toUpperCase().replaceAll("[^A-Z0-9]", "");
                // Garante que não ultrapasse o limite
                int espacoDisponivel = LIMITE - getLength();
                if (espacoDisponivel <= 0) return;
                if (filtrado.length() > espacoDisponivel) {
                    filtrado = filtrado.substring(0, espacoDisponivel);
                }
                super.insertString(offs, filtrado, a);
            }
        });
    }

    // ── Carregar clientes no ComboBox ─────────────────────────────────────────
    /**
     * Busca todos os clientes ativos no banco e popula o cmbCliente.
     * A ordem do item no combo corresponde à posição em listaClientes,
     * o que permite recuperar o ClienteModel pelo índice selecionado.
     */
    private void carregarClientes() {
        try {
            ClienteController clienteController = ContextoAplicacao.getBean(ClienteController.class);
            listaClientes = clienteController.findAll();

            cmbCliente.removeAllItems();
            if (listaClientes.isEmpty()) {
                cmbCliente.addItem("Nenhum cliente cadastrado");
            } else {
                cmbCliente.addItem("Selecione um cliente...");
                for (ClienteModel c : listaClientes) {
                    cmbCliente.addItem(c.getNomeCompleto() + " — " + c.getTelefone());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar clientes: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retorna o ClienteModel selecionado no combo, ou null se nenhum
     * cliente válido foi escolhido (índice 0 = placeholder).
     */
    private ClienteModel getClienteSelecionado() {
        int idx = cmbCliente.getSelectedIndex();
        // idx 0 = placeholder "Selecione...", idx >= 1 = cliente real
        if (listaClientes == null || listaClientes.isEmpty() || idx <= 0) return null;
        return listaClientes.get(idx - 1); // -1 para compensar o placeholder
    }

    // ── Grid de marca e modelo ─────────────────────────────────────────────────
    private JPanel criarGridMarcaModelo() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 14, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbMarca  = criarCombo(new String[]{"Selecione..."});
        cmbModelo = criarCombo(new String[]{"Selecione primeiro a marca..."});
        cmbModelo.setEnabled(false);

        grid.add(criarGrupoCombo("Marca",  cmbMarca));
        grid.add(criarGrupoCombo("Modelo", cmbModelo));
        return grid;
    }

    /**
     * Preenche e desabilita os combos de Marca e Modelo com os valores
     * já escolhidos nas telas de seleção. Também limpa os campos do form
     * e recarrega a lista de clientes.
     */
    public void preencherSelecoes() {
        String marca  = frame.getMarcaSelecionada();
        String modelo = frame.getModeloSelecionado();

        cmbMarca.removeAllItems();
        cmbMarca.addItem(marca.isEmpty() ? "Selecione..." : marca);
        cmbMarca.setEnabled(false);

        cmbModelo.removeAllItems();
        cmbModelo.addItem(modelo.isEmpty() ? "Selecione..." : modelo);
        cmbModelo.setEnabled(false);

        // Limpa os campos para um novo cadastro
        txtPlaca.setText("");
        txtCor.setText("");
        txtChassi.setText("");

        // Recarrega a lista de clientes (pode ter sido cadastrado um novo entre sessões)
        carregarClientes();
    }

    // ── Rodapé de ações ───────────────────────────────────────────────────────
    private JPanel criarRodapeAcoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        // BOTÃO TELA CADASTRO VEÍCULO DE CANCELAR E VOLTAR PARA LISTA DE CLIENTES
        JButton btnCancelar = criarBotaoOutline("Cancelar", 110, 36);
        btnCancelar.addActionListener(e -> frame.mostrarTela(MainFrame.TELA_LISTA_CLIENTES));

        // BOTÃO TELA CADASTRO VEÍCULO DE SALVAR VEÍCULO NO BANCO
        JButton btnSalvar = criarBotaoGold("Salvar veículo", 140, 36);
        btnSalvar.addActionListener(e -> salvarVeiculo());

        p.add(btnCancelar);
        p.add(btnSalvar);
        return p;
    }

    // ── Salvar no banco ────────────────────────────────────────────────────────
    private void salvarVeiculo() {
        // 1. Verifica se o idModelo foi definido nas telas de seleção
        Long idModelo = frame.getIdModeloSelecionado();
        if (idModelo == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma marca e um modelo antes de cadastrar o veículo.",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Coleta e normaliza os valores dos campos
        //    A placa pode ter hífen visual (ABC-1234); o backend normaliza,
        //    mas já enviamos limpa para manter consistência.
        String placa  = txtPlaca.getText().replace("-", "").toUpperCase().trim();
        String cor    = txtCor.getText().trim();
        String chassi = txtChassi.getText().trim(); // já está em maiúsculas pelo PlainDocument

        if (placa.isEmpty() || cor.isEmpty() || chassi.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos obrigatórios (*).",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Valida o cliente selecionado no combo
        ClienteModel clienteSelecionado = getClienteSelecionado();
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this,
                "Selecione um cliente proprietário válido.",
                "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 4. Monta e persiste o VeiculoModel
        try {
            VeiculoModel veiculo = new VeiculoModel();
            veiculo.setPlaca(placa);
            veiculo.setCor(cor);
            veiculo.setChassi(chassi);
            veiculo.setIdModelo(idModelo);
            veiculo.setIdCliente(clienteSelecionado.getId());

            VeiculoController veiculoController = ContextoAplicacao.getBean(VeiculoController.class);
            veiculoController.insert(veiculo);

            JOptionPane.showMessageDialog(this,
                "Veículo cadastrado com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            frame.mostrarTela(MainFrame.TELA_LISTA_CLIENTES);

        } catch (FieldValidationException | RuleValidationException valEx) {
            JOptionPane.showMessageDialog(this,
                valEx.getMessage(),
                "Erro de validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao salvar veículo: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────
    private JLabel criarLabelSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(MainFrame.COR_NAVY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

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

    private JPanel criarGrupoCombo(String label, JComboBox<String> combo) {
        JPanel g = new JPanel();
        g.setOpaque(false);
        g.setLayout(new BoxLayout(g, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(0x444444));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        combo.setFont(MainFrame.FONT_NORMAL);

        g.add(lbl);
        g.add(Box.createVerticalStrut(4));
        g.add(combo);
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

    private JComboBox<String> criarCombo(String[] itens) {
        JComboBox<String> c = new JComboBox<>(itens);
        c.setFont(MainFrame.FONT_NORMAL);
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(0, 34));
        return c;
    }

    private JButton criarBotaoOutline(String texto, int w, int h) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MainFrame.COR_NAVY);
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
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}