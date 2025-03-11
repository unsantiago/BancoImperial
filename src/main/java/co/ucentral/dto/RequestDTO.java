package co.ucentral.dto;

public class RequestDTO {
    private String tipo;
    private String identificacionCliente;
    private String numeroTarjeta;
    private String fechaDesde;
    private String fechaHasta;
    private double monto; // ✅ Debe ser double
    private String cvv;
    private String fechaVencimiento;
    private String producto; // ✅ Debe ser String

    public RequestDTO(String tipo, String identificacionCliente, String numeroTarjeta,
                      String fechaDesde, String fechaHasta, double monto,
                      String cvv, String fechaVencimiento, String producto) {
        this.tipo = tipo;
        this.identificacionCliente = identificacionCliente;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.monto = monto;  // ✅ Asegurarse de que sea double
        this.cvv = cvv;
        this.fechaVencimiento = fechaVencimiento;
        this.producto = producto; // ✅ Asegurarse de que sea String
    }

    public String getTipo() { return tipo; }
    public String getIdentificacionCliente() { return identificacionCliente; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public String getFechaDesde() { return fechaDesde; }
    public String getFechaHasta() { return fechaHasta; }
    public double getMonto() { return monto; }  // ✅ Retorna double
    public String getCvv() { return cvv; }
    public String getFechaVencimiento() { return fechaVencimiento; }
    public String getProducto() { return producto; } // ✅ Retorna String
}
