package com.pwdmanager.pwdmanager.dao;

import com.pwdmanager.pwdmanager.config.DatabaseConfig;
import com.pwdmanager.pwdmanager.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean save(User user) {
        String sql = "INSERT INTO users(name, email, salt, hashed_password) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getSalt());          // novo campo salt
            stmt.setString(4, user.getHashedPassword()); // hash da senha + salt
    
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("[ERRO] Nenhuma linha afetada ao inserir usuário.");
                return false;
            }
    
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    System.out.println("[ERRO] Falha ao obter ID do usuário inserido.");
                    return false;
                }
            }
    
            return true;
    
        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao verificar e-mail: " + e.getMessage());
            return false;
        }
    }

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setHashedPassword(rs.getString("hashedPassword"));
                return user;
            }

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return null;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setHashedPassword(rs.getString("hashedPassword"));
                return user;
            }

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao buscar usuário por e-mail: " + e.getMessage());
        }

        return null;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setHashedPassword(rs.getString("hashedPassword"));
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao listar usuários: " + e.getMessage());
        }

        return users;
    }

    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, hashedPassword = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getHashedPassword());
            stmt.setInt(4, user.getId());

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                System.out.println("[AVISO] Nenhum usuário atualizado. Verifique o ID.");
            }

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted == 0) {
                System.out.println("[AVISO] Nenhum usuário deletado. Verifique o ID.");
            }

        } catch (SQLException e) {
            System.err.println("[ERRO] Erro ao deletar usuário: " + e.getMessage());
        }
    }
}
