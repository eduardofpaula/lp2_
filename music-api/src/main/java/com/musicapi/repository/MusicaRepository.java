package com.musicapi.repository;

import com.musicapi.model.Musica;
import com.musicapi.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// implementação do repositório para Musica java puro, inserindo com sql
public class MusicaRepository implements Repository<Musica> {

    @Override
    public Musica save(Musica musica) {
        String sql = "INSERT INTO musica (titulo, duracao, album, ano_lancamento, artista_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, musica.getTitulo());
            stmt.setInt(2, musica.getDuracao());
            stmt.setString(3, musica.getAlbum());
            stmt.setObject(4, musica.getAnoLancamento());
            stmt.setLong(5, musica.getArtistaId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    musica.setId(rs.getLong(1));
                }
            }

            return musica;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar música: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Musica> findById(Long id) {
        String sql = "SELECT * FROM musica WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMusica(rs));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar música: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Musica> findAll() {
        String sql = "SELECT * FROM musica";
        List<Musica> musicas = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                musicas.add(mapearMusica(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar músicas: " + e.getMessage(), e);
        }

        return musicas;
    }

    public List<Musica> findByArtistaId(Long artistaId) {
        String sql = "SELECT * FROM musica WHERE artista_id = ?";
        List<Musica> musicas = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, artistaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    musicas.add(mapearMusica(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar músicas do artista: " + e.getMessage(), e);
        }

        return musicas;
    }

    @Override
    public Musica update(Long id, Musica musica) {
        String sql = "UPDATE musica SET titulo = ?, duracao = ?, album = ?, ano_lancamento = ?, artista_id = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, musica.getTitulo());
            stmt.setInt(2, musica.getDuracao());
            stmt.setString(3, musica.getAlbum());
            stmt.setObject(4, musica.getAnoLancamento());
            stmt.setLong(5, musica.getArtistaId());
            stmt.setLong(6, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Música não encontrada com id: " + id);
            }

            musica.setId(id);
            return musica;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar música: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM musica WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar música: " + e.getMessage(), e);
        }
    }

    private Musica mapearMusica(ResultSet rs) throws SQLException {
        Musica musica = new Musica();
        musica.setId(rs.getLong("id"));
        musica.setTitulo(rs.getString("titulo"));
        musica.setDuracao(rs.getInt("duracao"));
        musica.setAlbum(rs.getString("album"));

        Integer ano = rs.getObject("ano_lancamento", Integer.class);
        musica.setAnoLancamento(ano);

        musica.setArtistaId(rs.getLong("artista_id"));
        return musica;
    }
}