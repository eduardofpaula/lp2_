package com.musicapi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utilitário para conversão JSON <-> Objeto
 * Demonstra ENCAPSULAMENTO
 */
public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}