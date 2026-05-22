package br.com.picarauto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitário de conexão com o banco de dados SQLite.
 * Padrão Singleton — garante uma única instância de conexão.
 */
public class ConexaoBanco {

    private static final String URL = "jdbc:sqlite:database/oficina.db";
    private static Connection instancia = null;

    // Construtor privado — impede instanciação externa (Singleton)
    private ConexaoBanco() {}

    /**
     * Retorna a conexão ativa com o banco de dados.
     * Cria uma nova conexão caso não exista ou esteja fechada.
     */
    public static Connection getConexao() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection(URL);
            instancia.setAutoCommit(true);
        }
        return instancia;
    }

    /**
     * Encerra a conexão com o banco de dados.
     */
    public static void fecharConexao() {
        if (instancia != null) {
            try {
                instancia.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
