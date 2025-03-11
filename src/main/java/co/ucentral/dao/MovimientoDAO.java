package co.ucentral.dao;

import co.ucentral.dto.MovimientoDTO;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class MovimientoDAO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<MovimientoDTO> obtenerMovimientos(String numeroTarjeta, String identificacionCliente, String fechaDesde, String fechaHasta) {
        List<MovimientoDTO> movimientos = new ArrayList<>();

        // Primero, verificar que la tarjeta pertenece al cliente
        String validarClienteQuery = "SELECT COUNT(*) FROM tarjeta WHERE numero_tarjeta = ? AND cliente_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement validarStmt = conn.prepareStatement(validarClienteQuery)) {

            validarStmt.setString(1, numeroTarjeta);
            validarStmt.setString(2, identificacionCliente);
            ResultSet rsValidacion = validarStmt.executeQuery();

            if (!rsValidacion.next() || rsValidacion.getInt(1) == 0) {
                System.out.println("Error: La tarjeta no pertenece al cliente.");
                return movimientos; // Retorna lista vacía si no hay coincidencia
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return movimientos;
        }

        // Si la tarjeta pertenece al cliente, proceder con la consulta de movimientos
        String query = "SELECT m.* FROM movimiento m " +
                "JOIN tarjeta t ON m.numero_tarjeta = t.numero_tarjeta " +
                "WHERE m.numero_tarjeta = ? AND t.cliente_id = ? " +
                "AND m.fecha BETWEEN ? AND ? " +
                "ORDER BY m.fecha DESC";

        // Si no se ingresan fechas, usar el último mes automáticamente
        if (fechaDesde == null || fechaDesde.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);  // Un mes hacia atrás
            fechaDesde = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        }
        if (fechaHasta == null || fechaHasta.isEmpty()) {
            fechaHasta = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Fecha actual
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroTarjeta);
            stmt.setString(2, identificacionCliente);
            stmt.setString(3, fechaDesde);
            stmt.setString(4, fechaHasta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movimientos.add(new MovimientoDTO(
                        rs.getString("numero_tarjeta"),
                        rs.getString("fecha"),
                        rs.getDouble("valor"),
                        rs.getString("tipo_movimiento"),
                        rs.getString("establecimiento"),
                        rs.getString("terminal")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movimientos;
    }
    public void registrarPago(String numeroTarjeta, double monto) {
        String sql = "INSERT INTO movimiento (numeroTarjeta, fecha, valor, tipoMovimiento) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroTarjeta);
            pstmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setDouble(3, monto);
            pstmt.setString(4, "PAGO");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
