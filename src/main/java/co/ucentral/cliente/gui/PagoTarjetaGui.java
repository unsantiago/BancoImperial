package co.ucentral.cliente.gui;

import co.ucentral.dto.RequestDTO;
import co.ucentral.dto.ResponseDTO;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PagoTarjetaGui extends JFrame {
    private JTextField numeroTarjetaTxt;
    private JTextField identificacionTxt;
    private JTextField montoTxt;
    private JTextArea mensajesTxt;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Gson gson;

    public PagoTarjetaGui(PrintWriter out, BufferedReader in, Gson gson) {
        this.out = out;
        this.in = in;
        this.gson = gson;

        setTitle("Pago de Tarjeta");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblNumeroTarjeta = new JLabel("Número de Tarjeta:");
        lblNumeroTarjeta.setBounds(20, 20, 200, 20);
        add(lblNumeroTarjeta);

        numeroTarjetaTxt = new JTextField();
        numeroTarjetaTxt.setBounds(20, 40, 200, 30);
        add(numeroTarjetaTxt);

        JLabel lblIdentificacion = new JLabel("Identificación:");
        lblIdentificacion.setBounds(20, 80, 200, 20);
        add(lblIdentificacion);

        identificacionTxt = new JTextField();
        identificacionTxt.setBounds(20, 100, 200, 30);
        add(identificacionTxt);

        JLabel lblMonto = new JLabel("Monto a Pagar:");
        lblMonto.setBounds(20, 140, 200, 20);
        add(lblMonto);

        montoTxt = new JTextField();
        montoTxt.setBounds(20, 160, 200, 30);
        add(montoTxt);

        JButton btnPagar = new JButton("Pagar");
        btnPagar.setBounds(250, 160, 100, 30);
        btnPagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                realizarPago();
            }
        });
        add(btnPagar);

        mensajesTxt = new JTextArea();
        mensajesTxt.setBounds(20, 200, 350, 50);
        mensajesTxt.setEditable(false);
        add(mensajesTxt);
    }

    private void realizarPago() {
        // Obtener datos ingresados en la interfaz
        String numeroTarjeta = numeroTarjetaTxt.getText().trim();
        String identificacion = identificacionTxt.getText().trim();
        String montoTexto = montoTxt.getText().trim();

        // Validar que los campos no estén vacíos
        if (numeroTarjeta.isEmpty() || identificacion.isEmpty() || montoTexto.isEmpty()) {
            mensajesTxt.setText("⚠️ Todos los campos son obligatorios.");
            return;
        }

        // Validar que el monto sea un número válido y positivo
        double monto;
        try {
            monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                mensajesTxt.setText("⚠️ El monto debe ser mayor que 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mensajesTxt.setText("⚠️ Ingrese un monto válido.");
            return;
        }

        // Crear la solicitud de pago con el nuevo RequestDTO
        RequestDTO request = new RequestDTO(
                "PAGO_TARJETA",
                identificacion,
                numeroTarjeta,
                null, // No aplica fechaDesde
                null, // No aplica fechaHasta
                monto,
                null, // No aplica CVV
                null, // No aplica fecha de vencimiento
                null // No aplica producto
        );

        // Enviar la solicitud al servidor
        try {
            String jsonRequest = gson.toJson(request);
            out.println(jsonRequest);

            // Recibir la respuesta del servidor
            String jsonResponse = in.readLine();
            ResponseDTO response = gson.fromJson(jsonResponse, ResponseDTO.class);

            // Mostrar el mensaje de respuesta
            if ("OK".equals(response.getEstado())) {
                mensajesTxt.setText("✅ Pago realizado con éxito.\nNuevo cupo disponible: " + response.getCupoDisponible());
            } else {
                mensajesTxt.setText("❌ Error: " + response.getMensaje());
            }
        } catch (IOException e) {
            mensajesTxt.setText("❌ Error de conexión con el servidor.");
        }
    }

}
