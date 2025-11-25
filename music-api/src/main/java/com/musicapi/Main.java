package com.musicapi;

import com.musicapi.server.HttpServer;
import com.musicapi.util.DatabaseUtil;

// classe principal para iniciar a aplicação
public class Main {
    private static final int PORTA = 8080;

    public static void main(String[] args) {
        try {
            // Inicializa o banco de dados
            DatabaseUtil.getConnection();

            // Inicia o servidor HTTP
            HttpServer server = new HttpServer(PORTA);
            server.iniciar();

            // Adiciona hook para shutdown gracioso
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.parar();
                DatabaseUtil.closeConnection();
            }));

        } catch (Exception e) {
            System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}