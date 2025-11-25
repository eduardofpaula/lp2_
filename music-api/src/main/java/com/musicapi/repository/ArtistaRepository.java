package com.musicapi.repository;

import com.musicapi.model.Artista;
import com.musicapi.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// implementação do repositório para Artista java puro, inserindo com sql
public class ArtistaRepository implements Repository<Artista> {

    @Override
    public Artista save(Artista artista) {
        String sql = "INSERT INTO artista (nome, genero, pais_origem) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, artista.getNome());
            stmt.setString(2, artista.getGenero());
            stmt.setString(3, artista.getPaisOrigem());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    artista.setId(rs.getLong(1));
                }
            }

            return artista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar artista: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Artista> findById(Long id) {
        String sql = "SELECT * FROM artista WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearArtista(rs));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar artista: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Artista> findAll() {
        String sql = "SELECT * FROM artista";
        List<Artista> artistas = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artistas.add(mapearArtista(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar artistas: " + e.getMessage(), e);
        }

        return artistas;
    }

    @Override
    public Artista update(Long id, Artista artista) {
        String sql = "UPDATE artista SET nome = ?, genero = ?, pais_origem = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artista.getNome());
            stmt.setString(2, artista.getGenero());
            stmt.setString(3, artista.getPaisOrigem());
            stmt.setLong(4, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Artista não encontrado com id: " + id);
            }

            artista.setId(id);
            return artista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar artista: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM artista WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar artista: " + e.getMessage(), e);
        }
    }

    private Artista mapearArtista(ResultSet rs) throws SQLException {
        Artista artista = new Artista();
        artista.setId(rs.getLong("id"));
        artista.setNome(rs.getString("nome"));
        artista.setGenero(rs.getString("genero"));
        artista.setPaisOrigem(rs.getString("pais_origem"));
        return artista;
    }
}