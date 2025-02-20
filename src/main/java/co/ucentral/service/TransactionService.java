package co.ucentral.service;

import co.ucentral.dao.MovimientoDAO;
import co.ucentral.dao.TarjetaDAO;
import co.ucentral.dto.MovimientoDTO;
import co.ucentral.dto.ResponseDTO;
import co.ucentral.dto.TarjetaDTO;

import java.util.List;

public class TransactionService {

    private final MovimientoDAO movimientoDAO;
    private final TarjetaDAO tarjetaDAO;

    public TransactionService() {
        this.movimientoDAO = new MovimientoDAO();
        this.tarjetaDAO = new TarjetaDAO();
    }

    /**
     * Consulta los movimientos de una tarjeta de crédito.
     *
     * @param numeroTarjeta Número de la tarjeta.
     * @return ResponseDTO con la lista de movimientos o un mensaje de error.
     */
    public ResponseDTO consultarMovimientos(String numeroTarjeta, String identificacionCliente, String fechaDesde, String fechaHasta) {
        List<MovimientoDTO> movimientos = movimientoDAO.obtenerMovimientos(numeroTarjeta, identificacionCliente, fechaDesde, fechaHasta);
        if (movimientos != null && !movimientos.isEmpty()) {
            return new ResponseDTO("OK", "Consulta exitosa", 0, movimientos);
        } else {
            return new ResponseDTO("ERROR", "No se encontraron movimientos en el rango de fechas especificado.", 0, null);
        }
    }



    /**
     * Consulta el cupo disponible de una tarjeta de crédito.
     *
     * @param numeroTarjeta Número de la tarjeta.
     * @return ResponseDTO con el cupo disponible o un mensaje de error.
     */
    public ResponseDTO consultarCupoDisponible(String numeroTarjeta) {
        double cupoDisponible = tarjetaDAO.obtenerCupoDisponible(numeroTarjeta);
        if (cupoDisponible >= 0) {
            return new ResponseDTO("OK", "Cupo disponible consultado exitosamente", cupoDisponible, null);
        } else {
            return new ResponseDTO("ERROR", "Tarjeta no encontrada.", 0, null);
        }
    }


}

