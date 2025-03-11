package co.ucentral.server;

import com.google.gson.Gson;
import co.ucentral.dto.RequestDTO;
import co.ucentral.dto.ResponseDTO;
import co.ucentral.service.TransactionService;
import co.ucentral.service.PagoService;
import co.ucentral.service.CompraService; // ✅ Nueva importación

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private final TransactionService transactionService;
    private final PagoService pagoService;
    private final CompraService compraService; // ✅ Nueva instancia de CompraService
    private final Gson gson;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.transactionService = new TransactionService();
        this.pagoService = new PagoService();
        this.compraService = new CompraService(); // ✅ Inicializamos compraService
        this.gson = new Gson();

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Solicitud recibida: " + inputLine);

                // Convertir JSON a RequestDTO
                RequestDTO request = gson.fromJson(inputLine, RequestDTO.class);
                ResponseDTO response;

                // Determinar tipo de solicitud
                switch (request.getTipo()) {
                    case "CONSULTA_MOVIMIENTOS":
                        response = transactionService.consultarMovimientos(
                                request.getNumeroTarjeta(),
                                request.getIdentificacionCliente(),
                                request.getFechaDesde(),
                                request.getFechaHasta()
                        );
                        break;
                    case "CONSULTA_CUPO":
                        response = transactionService.consultarCupoDisponible(request.getNumeroTarjeta());
                        break;
                    case "PAGO_TARJETA":
                        response = pagoService.realizarPago(
                                request.getNumeroTarjeta(),
                                request.getIdentificacionCliente(),
                                request.getMonto());
                        break;
                    case "COMPRA_PRODUCTO": // ✅ Nueva funcionalidad de compra
                        response = compraService.realizarCompra(
                                request.getNumeroTarjeta(),
                                request.getIdentificacionCliente(),
                                request.getCvv(),
                                request.getFechaVencimiento(),
                                request.getMonto(),
                                request.getProducto());
                        break;
                    default:
                        response = new ResponseDTO("ERROR", "Tipo de solicitud no reconocido.", 0, null);
                        break;
                }

                // Convertir ResponseDTO a JSON y enviarlo al cliente
                String responseJson = gson.toJson(response);
                out.println(responseJson);
                System.out.println("Respuesta enviada: " + responseJson);
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
