package co.ucentral.dto;

public class ProductoDTO {
    private int id;
    private String nombre;
    private double precio;

    public ProductoDTO(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }
}
