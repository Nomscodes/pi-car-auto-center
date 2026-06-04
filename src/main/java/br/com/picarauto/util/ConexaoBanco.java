package br.com.picarauto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitário de conexão com o banco de dados.
 *
 * Padrão de Projeto: Singleton
 * Garante uma única instância de conexão durante a execução, evitando
 * abertura desnecessária de múltiplas conexões com o banco.
 *
 * @author Cassiano
 */
public class ConexaoBanco {
    private static final String URL = "jdbc:sqlite:database/picarauto.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
    
    public static Connection getConexao() {
        return conectar();
    }
}