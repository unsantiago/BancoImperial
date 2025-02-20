package co.ucentral.dto;


public class MovimientoDTO {
    private String numeroTarjeta;
    private String fecha; // Antes: Date fecha
    private double valor;
    private String tipoMovimiento;
    private String establecimiento;
    private String terminal;

    public MovimientoDTO(String numeroTarjeta, String fecha, double valor, String tipoMovimiento, String establecimiento, String terminal) {
        this.numeroTarjeta = numeroTarjeta;
        this.fecha = fecha; // Ahora la fecha se maneja como String
        this.valor = valor;
        this.tipoMovimiento = tipoMovimiento;
        this.establecimiento = establecimiento;
        this.terminal = terminal;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public String getFecha() {
        return fecha;
    }

    public double getValor() {
        return valor;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public String getTerminal() {
        return terminal;
    }
}
