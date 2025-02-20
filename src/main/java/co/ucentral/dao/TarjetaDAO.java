package co.ucentral.dao;

import co.ucentral.dto.TarjetaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarjetaDAO {
    public TarjetaDTO obtenerCupoDisponible(String numeroTarjeta) {
        String query = "SELECT * FROM tarjeta WHERE numero_tarjeta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroTarjeta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TarjetaDTO(
                        rs.getString("numero_tarjeta"),
                        rs.getString("cliente_id"),
                        rs.getDouble("cupo_total"),
                        rs.getDouble("cupo_disponible"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

