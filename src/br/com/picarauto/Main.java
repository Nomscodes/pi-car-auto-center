package br.com.picarauto;

import br.com.picarauto.util.ConexaoBanco;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Ponto de entrada da aplicação.
 * PI 2026/1 — SENAI FATESG — ADS 3º Período
 * Sistema de Controle de Oficina Mecânica — AV CAR AUTO CENTER
 */
public class Main {

    public static void main(String[] args) {

        // Garante que a interface Swing rode na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Define o look and feel do sistema operacional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Look and Feel não disponível: " + e.getMessage());
            }

            // Testa conexão com o banco na inicialização
            try {
                ConexaoBanco.getConexao();
                System.out.println("Banco de dados conectado com sucesso.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Erro ao conectar com o banco de dados:\n" + e.getMessage(),
                        "Erro de Conexão",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            // TODO: abrir tela principal
            // new TelaPrincipal().setVisible(true);

            System.out.println("AV CAR AUTO CENTER — Sistema iniciado.");
        });
    }
}
