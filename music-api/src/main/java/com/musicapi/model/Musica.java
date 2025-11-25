package com.musicapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade Música - lado "N" do relacionamento 1:N
 * Demonstra HERANÇA e ENCAPSULAMENTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Musica extends BaseEntity {
    private String titulo;
    private Integer duracao; // duração em segundos
    private String album;
    private Integer anoLancamento;
    private Long artistaId; // FK para Artista

    /**
     * Implementação de método abstrato - demonstra POLIMORFISMO
     */
    @Override
    public boolean isValid() {
        return titulo != null && !titulo.trim().isEmpty()
                && duracao != null && duracao > 0
                && artistaId != null;
    }

    public String getDuracaoFormatada() {
        if (duracao == null) return "00:00";
        int minutos = duracao / 60;
        int segundos = duracao % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
}