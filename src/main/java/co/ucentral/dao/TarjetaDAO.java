package co.ucentral.dao;

import co.ucentral.dto.TarjetaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarjetaDAO {
    public double obtenerCupoDisponible(String numeroTarjeta) {
        String query = "SELECT cupo_disponible FROM tarjeta WHERE numero_tarjeta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroTarjeta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double cupo = rs.getDouble("cupo_disponible");
                System.out.println("Cupo disponible en DAO: " + cupo); // Agregar esto
                return cupo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Si no encuentra la tarjeta, devuelve -1
    }



}

