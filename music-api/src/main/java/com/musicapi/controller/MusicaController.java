package com.musicapi.controller;

import com.musicapi.model.Musica;
import com.musicapi.service.MusicaService;
import com.musicapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para Música
 * Demonstra separação de responsabilidades e POLIMORFISMO
 */
public class MusicaController implements Controller {
    private final MusicaService musicaService;

    public MusicaController() {
        this.musicaService = new MusicaService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange, path);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    enviarResposta(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }
        } catch (IllegalArgumentException e) {
            enviarResposta(exchange, 400, "{\"erro\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            enviarResposta(exchange, 500, "{\"erro\":\"Erro interno do servidor\"}");
            e.printStackTrace();
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] partes = path.split("/");

        // GET /musicas - Listar todas
        if (partes.length == 2) {
            List<Musica> musicas = musicaService.listarTodas();
            String json = JsonUtil.toJson(musicas);
            enviarResposta(exchange, 200, json);
        }
        // GET /musicas/{id} - Buscar por ID
        else if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            Optional<Musica> musica = musicaService.buscarPorId(id);

            if (musica.isPresent()) {
                String json = JsonUtil.toJson(musica.get());
                enviarResposta(exchange, 200, json);
            } else {
                enviarResposta(exchange, 404, "{\"erro\":\"Música não encontrada\"}");
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = lerBody(exchange);
        Musica musica = JsonUtil.fromJson(body, Musica.class);
        Musica musicaSalva = musicaService.criar(musica);
        String json = JsonUtil.toJson(musicaSalva);
        enviarResposta(exchange, 201, json);
    }

    private void handlePut(HttpExchange exchange, String path) throws IOException {
        String[] partes = path.split("/");

        if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            String body = lerBody(exchange);
            Musica musica = JsonUtil.fromJson(body, Musica.class);
            Musica musicaAtualizada = musicaService.atualizar(id, musica);
            String json = JsonUtil.toJson(musicaAtualizada);
            enviarResposta(exchange, 200, json);
        } else {
            enviarResposta(exchange, 400, "{\"erro\":\"ID não fornecido\"}");
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] partes = path.split("/");

        if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            boolean deletado = musicaService.deletar(id);

            if (deletado) {
                enviarResposta(exchange, 204, "");
            } else {
                enviarResposta(exchange, 404, "{\"erro\":\"Música não encontrada\"}");
            }
        } else {
            enviarResposta(exchange, 400, "{\"erro\":\"ID não fornecido\"}");
        }
    }

    private String lerBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void enviarResposta(HttpExchange exchange, int statusCode, String resposta) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);

        if (bytes.length > 0) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            exchange.getResponseBody().close();
        }
    }
}