package com.musicapi.server;

import com.musicapi.controller.ArtistaController;
import com.musicapi.controller.MusicaController;

import java.io.IOException;
import java.net.InetSocketAddress;

// servidor HTTP simples
public class HttpServer {
    private final com.sun.net.httpserver.HttpServer server;

    public HttpServer(int porta) throws IOException {
        this.server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(porta), 0);
        configurarRotas();
    }

    private void configurarRotas() {
        ArtistaController artistaController = new ArtistaController();
        MusicaController musicaController = new MusicaController();

        // Rotas para Artistas
        server.createContext("/artistas", artistaController::handle);

        // Rotas para Músicas
        server.createContext("/musicas", musicaController::handle);

        // Rota raiz
        server.createContext("/", exchange -> {
            String resposta = "{\n" +
                    "  \"mensagem\": \"API de Gerenciamento Musical\",\n" +
                    "  \"endpoints\": [\n" +
                    "    \"GET    /artistas - Listar todos os artistas\",\n" +
                    "    \"GET    /artistas/{id} - Buscar artista por ID (com músicas)\",\n" +
                    "    \"POST   /artistas - Criar novo artista\",\n" +
                    "    \"PUT    /artistas/{id} - Atualizar artista\",\n" +
                    "    \"DELETE /artistas/{id} - Deletar artista\",\n" +
                    "    \"\",\n" +
                    "    \"GET    /musicas - Listar todas as músicas\",\n" +
                    "    \"GET    /musicas/{id} - Buscar música por ID\",\n" +
                    "    \"POST   /musicas - Criar nova música\",\n" +
                    "    \"PUT    /musicas/{id} - Atualizar música\",\n" +
                    "    \"DELETE /musicas/{id} - Deletar música\"\n" +
                    "  ]\n" +
                    "}";

            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            byte[] bytes = resposta.getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        });
    }

    public void iniciar() {
        server.start();
        System.out.println("========================================");
        System.out.println("  🎵 Music API está rodando! 🎵");
        System.out.println("========================================");
        System.out.println("  Porta: " + server.getAddress().getPort());
        System.out.println("  URL: http://localhost:" + server.getAddress().getPort());
        System.out.println("========================================");
        System.out.println("\nEndpoints disponíveis:");
        System.out.println("  GET    http://localhost:" + server.getAddress().getPort() + "/artistas");
        System.out.println("  POST   http://localhost:" + server.getAddress().getPort() + "/artistas");
        System.out.println("  GET    http://localhost:" + server.getAddress().getPort() + "/musicas");
        System.out.println("  POST   http://localhost:" + server.getAddress().getPort() + "/musicas");
        System.out.println("\nPressione Ctrl+C para parar o servidor.\n");
    }

    public void parar() {
        server.stop(0);
        System.out.println("\n✓ Servidor parado.");
    }
}