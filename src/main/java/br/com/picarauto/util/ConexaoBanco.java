package br.com.picarauto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitário de conexão com o banco de dados PostgreSQL.
 * Padrão Singleton para reutilização da conexão.
 */
public class ConexaoBanco {

    private static final String URL     = "jdbc:postgresql://localhost:5432/pi_car_auto_center";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "postgres";

    private static Connection instancia = null;

    private ConexaoBanco() {}

    public static Connection getConexao() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return instancia;
    }
}