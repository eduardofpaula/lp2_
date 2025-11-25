package com.musicapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// entidade artista com herança e encapsulamento
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Artista extends BaseEntity {
    private String nome;
    private String genero;
    private String paisOrigem;

    // relacionamento 1:N
    private List<Musica> musicas = new ArrayList<>();

    public Artista(String nome, String genero, String paisOrigem) {
        this.nome = nome;
        this.genero = genero;
        this.paisOrigem = paisOrigem;
    }

    // metodo abstrato de polimorfismo
    @Override
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty()
                && genero != null && !genero.trim().isEmpty();
    }

//    public void adicionarMusica(Musica musica) {
//        if (musica != null) {
//            musicas.add(musica);
//            musica.setArtistaId(this.id);
//        }
//    }
}