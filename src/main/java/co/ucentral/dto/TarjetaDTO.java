package co.ucentral.dto;

public class TarjetaDTO {
    private String numeroTarjeta;
    private String clienteId;
    private double cupoTotal;
    private double cupoDisponible;

    public TarjetaDTO(String numeroTarjeta, String clienteId, double cupoTotal, double cupoDisponible) {
        this.numeroTarjeta = numeroTarjeta;
        this.clienteId = clienteId;
        this.cupoTotal = cupoTotal;
        this.cupoDisponible = cupoDisponible;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public double getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(double cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public double getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(double cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }
}

