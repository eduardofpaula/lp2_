package com.musicapi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utilitário para gerenciar conexão com H2 Database
 * Demonstra ENCAPSULAMENTO e baixo acoplamento
 */
public class DatabaseUtil {
    private static final String URL = "jdbc:h2:mem:musicdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            inicializarTabelas();
        }
        return connection;
    }

    private static void inicializarTabelas() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Tabela Artista
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS artista (" +
                            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                            "nome VARCHAR(255) NOT NULL, " +
                            "genero VARCHAR(100), " +
                            "pais_origem VARCHAR(100)" +
                            ")"
            );

            // Tabela Música com FK para Artista (relacionamento 1:N)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS musica (" +
                            "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                            "titulo VARCHAR(255) NOT NULL, " +
                            "duracao INTEGER, " +
                            "album VARCHAR(255), " +
                            "ano_lancamento INTEGER, " +
                            "artista_id BIGINT NOT NULL, " +
                            "FOREIGN KEY (artista_id) REFERENCES artista(id) ON DELETE CASCADE" +
                            ")"
            );

            System.out.println("✓ Banco de dados H2 inicializado com sucesso!");
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexão com banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}