package com.musicapi.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

// interface base para controllers
public interface Controller {
    void handle(HttpExchange exchange) throws IOException;
}