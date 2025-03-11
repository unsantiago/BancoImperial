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

    public boolean actualizarCupoDisponible(String numeroTarjeta, double montoPago) {
        String sql = "UPDATE tarjeta SET cupo_disponible = cupo_disponible + ? WHERE numero_tarjeta = ? AND (cupo_total - cupo_disponible) >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, montoPago);
            pstmt.setString(2, numeroTarjeta);
            pstmt.setDouble(3, montoPago);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Si se actualizó al menos una fila, el pago fue exitoso
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TarjetaDTO obtenerTarjeta(String numeroTarjeta) {
        String query = "SELECT numero_tarjeta, cliente_id, cupo_total, cupo_disponible, cvv, fecha_vencimiento FROM tarjeta WHERE numero_tarjeta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroTarjeta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                TarjetaDTO tarjeta = new TarjetaDTO();
                tarjeta.setNumeroTarjeta(rs.getString("numero_tarjeta"));
                tarjeta.setIdentificacionCliente(rs.getString("cliente_id"));
                tarjeta.setCupoTotal(rs.getDouble("cupo_total"));
                tarjeta.setCupoDisponible(rs.getDouble("cupo_disponible"));
                tarjeta.setCvv(rs.getString("cvv")); // ✅ Corrección aquí
                tarjeta.setFechaVencimiento(rs.getString("fecha_vencimiento"));
                return tarjeta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la tarjeta, devuelve null
    }



}

