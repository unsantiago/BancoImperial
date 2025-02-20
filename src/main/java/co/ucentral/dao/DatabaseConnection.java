package co.ucentral.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:/Users/Santiago/db SQLite/BancoImperial.db";
    private static Connection connection;

    private DatabaseConnection() {} // Constructor privado para evitar instancias múltiples

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Conectado a la base de datos: " + URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}


