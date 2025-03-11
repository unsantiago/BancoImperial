package co.ucentral.dto;

public class TarjetaDTO {
    private String numeroTarjeta;
    private String identificacionCliente;
    private double cupoTotal;
    private double cupoDisponible;
    private String cvv;  // ✅ Nuevo campo
    private String fechaVencimiento;  // ✅ Nuevo campo

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }

    public double getCupoTotal() { return cupoTotal; }
    public void setCupoTotal(double cupoTotal) { this.cupoTotal = cupoTotal; }

    public double getCupoDisponible() { return cupoDisponible; }
    public void setCupoDisponible(double cupoDisponible) { this.cupoDisponible = cupoDisponible; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
