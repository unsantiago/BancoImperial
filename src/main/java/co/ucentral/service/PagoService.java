package co.ucentral.service;

import co.ucentral.dao.MovimientoDAO;
import co.ucentral.dao.TarjetaDAO;
import co.ucentral.dto.ResponseDTO;
import co.ucentral.dto.TarjetaDTO;

public class PagoService {
    private final MovimientoDAO movimientoDAO;
    private final TarjetaDAO tarjetaDAO;

    public PagoService() {
        this.movimientoDAO = new MovimientoDAO();
        this.tarjetaDAO = new TarjetaDAO();
    }

    public ResponseDTO realizarPago(String numeroTarjeta, String identificacion, double monto) {
        TarjetaDTO tarjeta = tarjetaDAO.obtenerTarjeta(numeroTarjeta);

        if (tarjeta == null) {
            return new ResponseDTO("ERROR", "Tarjeta no encontrada en la base de datos.", 0, null);
        }

        System.out.println("Número de tarjeta en BD: " + tarjeta.getNumeroTarjeta());
        System.out.println("Cliente en BD: " + tarjeta.getIdentificacionCliente());
        System.out.println("Cliente ingresado: " + identificacion);

// Validación corregida
        if (!tarjeta.getIdentificacionCliente().equals(identificacion)) {
            return new ResponseDTO("ERROR", "Tarjeta no encontrada o no pertenece al cliente.", 0, null);
        }


        double deudaActual = tarjeta.getCupoTotal() - tarjeta.getCupoDisponible();
        if (monto > deudaActual) {
            return new ResponseDTO("ERROR", "El monto del pago no puede superar la deuda actual.", 0, null);
        }

        boolean actualizado = tarjetaDAO.actualizarCupoDisponible(numeroTarjeta, monto);
        if (actualizado) {
            movimientoDAO.registrarPago(numeroTarjeta, monto);
            return new ResponseDTO("OK", "Pago realizado exitosamente.", tarjeta.getCupoDisponible(), null);
        } else {
            return new ResponseDTO("ERROR", "No se pudo procesar el pago.", 0, null);
        }
    }
}


