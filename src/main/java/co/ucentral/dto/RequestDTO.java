package co.ucentral.dto;

public class RequestDTO {
    private String tipo; // Puede ser "CONSULTA_MOVIMIENTOS" o "CONSULTA_CUPO"
    private String identificacionCliente;
    private String numeroTarjeta;
    private String fechaDesde;
    private String fechaHasta;
    private double monto;


    public RequestDTO(String tipo, String identificacionCliente, String numeroTarjeta, String fechaDesde, String fechaHasta, double monto) {
        this.tipo = tipo;
        this.identificacionCliente = identificacionCliente;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.monto = monto;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

}

