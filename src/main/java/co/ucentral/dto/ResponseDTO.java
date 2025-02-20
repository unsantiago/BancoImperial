package co.ucentral.dto;

import java.util.List;

public class ResponseDTO {
    private String estado; // "OK" o "ERROR"
    private String mensaje;
    private double cupoDisponible; // Solo se usa en consulta de cupo
    private List<MovimientoDTO> movimientos; // Solo se usa en consulta de movimientos

    public ResponseDTO(String estado, String mensaje, double cupoDisponible, List<MovimientoDTO> movimientos) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.cupoDisponible = cupoDisponible;
        this.movimientos = movimientos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public double getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(double cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }

    public List<MovimientoDTO> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoDTO> movimientos) {
        this.movimientos = movimientos;
    }
}

