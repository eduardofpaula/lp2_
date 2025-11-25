package com.musicapi.repository;

import com.musicapi.model.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface Repository genérica
 * Demonstra ABSTRAÇÃO e baixo acoplamento
 */
public interface Repository<T extends BaseEntity> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    T update(Long id, T entity);
    boolean delete(Long id);
}