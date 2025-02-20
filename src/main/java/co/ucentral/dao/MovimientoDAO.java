package co.ucentral.dao;

import co.ucentral.dto.MovimientoDTO;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovimientoDAO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<MovimientoDTO> obtenerMovimientos(String numeroTarjeta, String identificacionCliente) {
        List<MovimientoDTO> movimientos = new ArrayList<>();
        String query = "SELECT m.* FROM movimiento m " +
                "JOIN tarjeta t ON m.numero_tarjeta = t.numero_tarjeta " +
                "WHERE m.numero_tarjeta = ? AND t.cliente_id = ? " +
                "ORDER BY m.fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroTarjeta);
            stmt.setString(2, identificacionCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Convertir la fecha al formato ISO 8601
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaDate = inputFormat.parse(rs.getString("fecha"));
                String fechaFormateada = dateFormat.format(fechaDate);

                movimientos.add(new MovimientoDTO(
                        rs.getString("numero_tarjeta"),
                        fechaFormateada,  // Fecha en formato ISO
                        rs.getDouble("valor"),
                        rs.getString("tipo_movimiento"),
                        rs.getString("establecimiento"),
                        rs.getString("terminal")
                ));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return movimientos;
    }
}
