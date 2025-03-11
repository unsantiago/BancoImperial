package co.ucentral.service;

import co.ucentral.dao.MovimientoDAO;
import co.ucentral.dao.TarjetaDAO;
import co.ucentral.dto.ResponseDTO;
import co.ucentral.dto.TarjetaDTO;

public class CompraService {
    private final TarjetaDAO tarjetaDAO;
    private final MovimientoDAO movimientoDAO;

    public CompraService() {
        this.tarjetaDAO = new TarjetaDAO();
        this.movimientoDAO = new MovimientoDAO();
    }

    public ResponseDTO realizarCompra(String numeroTarjeta, String identificacionCliente, String cvv, String fechaVencimiento, double monto, String producto) {
        TarjetaDTO tarjeta = tarjetaDAO.obtenerTarjeta(numeroTarjeta);

        if (tarjeta == null) {
            return new ResponseDTO("ERROR", "Tarjeta no encontrada.", 0, null);
        }

        if (!tarjeta.getIdentificacionCliente().equals(identificacionCliente)) {
            return new ResponseDTO("ERROR", "Tarjeta no pertenece al cliente.", 0, null);
        }

        if (!tarjeta.getCvv().equals(cvv)) {
            return new ResponseDTO("ERROR", "CVV incorrecto.", 0, null);
        }

        if (!tarjeta.getFechaVencimiento().equals(fechaVencimiento)) {
            return new ResponseDTO("ERROR", "Fecha de vencimiento incorrecta.", 0, null);
        }

        if (monto > tarjeta.getCupoDisponible()) {
            return new ResponseDTO("ERROR", "Saldo insuficiente.", 0, null);
        }

        boolean actualizado = tarjetaDAO.actualizarCupoDisponible(numeroTarjeta, -monto);
        if (actualizado) {
            movimientoDAO.registrarCompra(numeroTarjeta, monto, producto);

            // 🔹 **Volver a consultar el cupo después de actualizar**
            TarjetaDTO tarjetaActualizada = tarjetaDAO.obtenerTarjeta(numeroTarjeta);

            return new ResponseDTO("OK", "Compra realizada con éxito.", tarjetaActualizada.getCupoDisponible(), null);
        } else {
            return new ResponseDTO("ERROR", "No se pudo procesar la compra.", 0, null);
        }
    }


}
