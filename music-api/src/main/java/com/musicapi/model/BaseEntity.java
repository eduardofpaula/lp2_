package com.musicapi.model;

import lombok.Data;

// classe base para entidades com encapsulamento
@Data
public abstract class BaseEntity {
    protected Long id;

    public abstract boolean isValid();
}