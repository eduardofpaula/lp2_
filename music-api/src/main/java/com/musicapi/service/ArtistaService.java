package com.musicapi.service;

import com.musicapi.model.Artista;
import com.musicapi.model.Musica;
import com.musicapi.repository.ArtistaRepository;
import com.musicapi.repository.MusicaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio de Artista
 * Demonstra separação de responsabilidades e baixo acoplamento
 */
public class ArtistaService {
    private final ArtistaRepository artistaRepository;
    private final MusicaRepository musicaRepository;

    public ArtistaService() {
        this.artistaRepository = new ArtistaRepository();
        this.musicaRepository = new MusicaRepository();
    }

    public Artista criar(Artista artista) {
        if (!artista.isValid()) {
            throw new IllegalArgumentException("Dados do artista inválidos");
        }
        return artistaRepository.save(artista);
    }

    public Optional<Artista> buscarPorId(Long id) {
        return artistaRepository.findById(id);
    }

    public Optional<Artista> buscarComMusicas(Long id) {
        Optional<Artista> artistaOpt = artistaRepository.findById(id);

        if (artistaOpt.isPresent()) {
            Artista artista = artistaOpt.get();
            List<Musica> musicas = musicaRepository.findByArtistaId(id);
            artista.setMusicas(musicas);
            return Optional.of(artista);
        }

        return Optional.empty();
    }

    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    public Artista atualizar(Long id, Artista artista) {
        if (!artista.isValid()) {
            throw new IllegalArgumentException("Dados do artista inválidos");
        }

        if (!artistaRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Artista não encontrado");
        }

        return artistaRepository.update(id, artista);
    }

    public boolean deletar(Long id) {
        return artistaRepository.delete(id);
    }
}