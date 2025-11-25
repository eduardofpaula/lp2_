package com.musicapi.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Interface Controller - demonstra ABSTRAÇÃO e POLIMORFISMO
 */
public interface Controller {
    void handle(HttpExchange exchange) throws IOException;
}