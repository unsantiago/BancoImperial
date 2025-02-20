package co.ucentral.dao;

import co.ucentral.dto.ClienteDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAO {
    public ClienteDTO obtenerCliente(String identificacion) {
        String query = "SELECT * FROM cliente WHERE identificacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, identificacion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ClienteDTO(rs.getString("identificacion"), rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
