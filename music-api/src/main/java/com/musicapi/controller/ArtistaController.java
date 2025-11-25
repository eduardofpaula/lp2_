package com.musicapi.controller;

import com.musicapi.model.Artista;
import com.musicapi.service.ArtistaService;
import com.musicapi.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

// controoler artista
public class ArtistaController implements Controller {
    private final ArtistaService artistaService;

    public ArtistaController() {
        this.artistaService = new ArtistaService();
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

        // GET /artistas - Listar todos
        if (partes.length == 2) {
            List<Artista> artistas = artistaService.listarTodos();
            String json = JsonUtil.toJson(artistas);
            enviarResposta(exchange, 200, json);
        }
        // GET /artistas/{id} - Buscar por ID
        else if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            Optional<Artista> artista = artistaService.buscarComMusicas(id);

            if (artista.isPresent()) {
                String json = JsonUtil.toJson(artista.get());
                enviarResposta(exchange, 200, json);
            } else {
                enviarResposta(exchange, 404, "{\"erro\":\"Artista não encontrado\"}");
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = lerBody(exchange);
        Artista artista = JsonUtil.fromJson(body, Artista.class);
        Artista artistaSalvo = artistaService.criar(artista);
        String json = JsonUtil.toJson(artistaSalvo);
        enviarResposta(exchange, 201, json);
    }

    private void handlePut(HttpExchange exchange, String path) throws IOException {
        String[] partes = path.split("/");

        if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            String body = lerBody(exchange);
            Artista artista = JsonUtil.fromJson(body, Artista.class);
            Artista artistaAtualizado = artistaService.atualizar(id, artista);
            String json = JsonUtil.toJson(artistaAtualizado);
            enviarResposta(exchange, 200, json);
        } else {
            enviarResposta(exchange, 400, "{\"erro\":\"ID não fornecido\"}");
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] partes = path.split("/");

        if (partes.length == 3) {
            Long id = Long.parseLong(partes[2]);
            boolean deletado = artistaService.deletar(id);

            if (deletado) {
                enviarResposta(exchange, 204, "");
            } else {
                enviarResposta(exchange, 404, "{\"erro\":\"Artista não encontrado\"}");
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