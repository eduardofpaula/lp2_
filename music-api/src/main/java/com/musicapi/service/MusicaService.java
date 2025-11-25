package com.musicapi.service;

import com.musicapi.model.Musica;
import com.musicapi.repository.ArtistaRepository;
import com.musicapi.repository.MusicaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service para lógica de negócio de Música
 * Demonstra separação de responsabilidades e baixo acoplamento
 */
public class MusicaService {
    private final MusicaRepository musicaRepository;
    private final ArtistaRepository artistaRepository;

    public MusicaService() {
        this.musicaRepository = new MusicaRepository();
        this.artistaRepository = new ArtistaRepository();
    }

    public Musica criar(Musica musica) {
        if (!musica.isValid()) {
            throw new IllegalArgumentException("Dados da música inválidos");
        }

        // Verifica se o artista existe
        if (!artistaRepository.findById(musica.getArtistaId()).isPresent()) {
            throw new IllegalArgumentException("Artista não encontrado");
        }

        return musicaRepository.save(musica);
    }

    public Optional<Musica> buscarPorId(Long id) {
        return musicaRepository.findById(id);
    }

    public List<Musica> listarTodas() {
        return musicaRepository.findAll();
    }

    public List<Musica> listarPorArtista(Long artistaId) {
        return musicaRepository.findByArtistaId(artistaId);
    }

    public Musica atualizar(Long id, Musica musica) {
        if (!musica.isValid()) {
            throw new IllegalArgumentException("Dados da música inválidos");
        }

        if (!musicaRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Música não encontrada");
        }

        // Verifica se o artista existe
        if (!artistaRepository.findById(musica.getArtistaId()).isPresent()) {
            throw new IllegalArgumentException("Artista não encontrado");
        }

        return musicaRepository.update(id, musica);
    }

    public boolean deletar(Long id) {
        return musicaRepository.delete(id);
    }
}