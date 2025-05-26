package com.pwdmanager.pwdmanager.dao;

import com.pwdmanager.pwdmanager.config.DatabaseConfig;
import com.pwdmanager.pwdmanager.model.PasswordEntry;
import com.pwdmanager.pwdmanager.util.CryptoUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {

    public void save(PasswordEntry entry) {
        String sql = "INSERT INTO passwords(user_id, service, username, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entry.getUserId());
            stmt.setString(2, entry.getService());
            stmt.setString(3, entry.getUsername());
            stmt.setString(4, CryptoUtils.encrypt(entry.getPassword())); // Criptografar antes de salvar
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir entrada de senha.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entry.setId(generatedKeys.getInt(1));
                    System.out.println("[DEBUG] Entrada salva com ID: " + entry.getId());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PasswordEntry findById(int id) {
        String sql = "SELECT * FROM passwords WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PasswordEntry entry = new PasswordEntry();
                entry.setId(rs.getInt("id"));
                entry.setUserId(rs.getInt("user_id"));
                entry.setService(rs.getString("service"));
                entry.setUsername(rs.getString("username"));
                entry.setPassword(CryptoUtils.decrypt(rs.getString("password"))); // Descriptografar ao buscar
                return entry;
            } else {
                System.out.println("[DEBUG] findById: Nenhuma entrada encontrada para id = " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PasswordEntry> findAllByUserId(int userId) {
        List<PasswordEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM passwords WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PasswordEntry entry = new PasswordEntry();
                entry.setId(rs.getInt("id"));
                entry.setUserId(rs.getInt("user_id"));
                entry.setService(rs.getString("service"));
                entry.setUsername(rs.getString("username"));
                entry.setPassword(CryptoUtils.decrypt(rs.getString("password"))); // Descriptografar
                entries.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public void update(PasswordEntry entry) {
        String sql = "UPDATE passwords SET service = ?, username = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entry.getService());
            stmt.setString(2, entry.getUsername());
            stmt.setString(3, CryptoUtils.encrypt(entry.getPassword())); // Criptografar
            stmt.setInt(4, entry.getId());

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                System.out.println("[DEBUG] update: Nenhuma entrada atualizada para id = " + entry.getId());
            } else {
                System.out.println("[DEBUG] Entrada com id = " + entry.getId() + " atualizada com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM passwords WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted == 0) {
                System.out.println("[DEBUG] delete: Nenhuma entrada deletada para id = " + id);
            } else {
                System.out.println("[DEBUG] Entrada com id = " + id + " deletada com sucesso.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
