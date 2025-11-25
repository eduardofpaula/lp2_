package com.musicapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Artista - lado "1" do relacionamento 1:N
 * Demonstra HERANÇA (extends BaseEntity) e ENCAPSULAMENTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Artista extends BaseEntity {
    private String nome;
    private String genero;
    private String paisOrigem;

    // Relacionamento 1:N - Um artista tem várias músicas
    private List<Musica> musicas = new ArrayList<>();

    public Artista(String nome, String genero, String paisOrigem) {
        this.nome = nome;
        this.genero = genero;
        this.paisOrigem = paisOrigem;
    }

    /**
     * Implementação de método abstrato - demonstra POLIMORFISMO
     */
    @Override
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty()
                && genero != null && !genero.trim().isEmpty();
    }

    public void adicionarMusica(Musica musica) {
        if (musica != null) {
            musicas.add(musica);
            musica.setArtistaId(this.id);
        }
    }
}