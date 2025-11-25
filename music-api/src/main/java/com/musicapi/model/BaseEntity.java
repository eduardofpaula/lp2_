package com.musicapi.model;

import lombok.Data;

/**
 * Classe base abstrata para todas as entidades
 * Demonstra ABSTRAÇÃO e HERANÇA
 */
@Data
public abstract class BaseEntity {
    protected Long id;

    public abstract boolean isValid();
}