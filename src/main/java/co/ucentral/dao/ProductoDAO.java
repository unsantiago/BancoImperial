package co.ucentral.dao;

import co.ucentral.dto.ProductoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public List<ProductoDTO> obtenerProductos() {
        List<ProductoDTO> productos = new ArrayList<>();
        String query = "SELECT * FROM producto";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(new ProductoDTO(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
}
