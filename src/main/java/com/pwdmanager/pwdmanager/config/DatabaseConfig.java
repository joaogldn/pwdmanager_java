package com.pwdmanager.pwdmanager.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:sqlite:pwd_manager.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC"); 
        } catch (ClassNotFoundException e) {
            System.err.println("[ERRO] Driver SQLite não encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("[INFO] Conexão com SQLite estabelecida.");
        return conn;
    }
}
