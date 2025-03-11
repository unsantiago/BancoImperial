package co.ucentral.cliente.gui;

import co.ucentral.dto.RequestDTO;
import co.ucentral.dto.ResponseDTO;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CompraGui extends JFrame {
    private JTextField numeroTarjetaTxt, cvvTxt, fechaTxt;
    private JComboBox<String> productoCombo;
    private JTextArea mensajesTxt;
    private final Gson gson;
    private final PrintWriter out;
    private final BufferedReader in;

    public CompraGui(Socket socket, PrintWriter out, BufferedReader in, Gson gson) {
        this.out = out;
        this.in = in;
        this.gson = gson;

        setTitle("Realizar Compra");
        setSize(450, 350); // 🔹 Se aumentó el tamaño de la ventana
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblNumeroTarjeta = new JLabel("Número de Tarjeta:");
        lblNumeroTarjeta.setBounds(20, 20, 200, 20);
        add(lblNumeroTarjeta);

        numeroTarjetaTxt = new JTextField();
        numeroTarjetaTxt.setBounds(20, 40, 200, 30);
        add(numeroTarjetaTxt);

        JLabel lblCvv = new JLabel("CVV:");
        lblCvv.setBounds(20, 80, 200, 20);
        add(lblCvv);

        cvvTxt = new JTextField();
        cvvTxt.setBounds(20, 100, 100, 30);
        add(cvvTxt);

        JLabel lblFecha = new JLabel("Fecha Vencimiento (MM/AA):");
        lblFecha.setBounds(20, 140, 200, 20);
        add(lblFecha);

        fechaTxt = new JTextField();
        fechaTxt.setBounds(20, 160, 100, 30);
        add(fechaTxt);

        productoCombo = new JComboBox<>(new String[]{"Netflix - $1.000", "Disney+ - $2.000", "HBO Max - $3.500"});
        productoCombo.setBounds(20, 200, 200, 30);
        add(productoCombo);

        JButton btnComprar = new JButton("Comprar");
        btnComprar.setBounds(250, 200, 120, 30); // 🔹 Botón más grande
        btnComprar.addActionListener(e -> realizarCompra());
        add(btnComprar);

        // 🔹 Área de mensajes con scroll
        mensajesTxt = new JTextArea();
        mensajesTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajesTxt);
        scrollPane.setBounds(20, 240, 400, 80); // 🔹 Más espacio para los mensajes
        add(scrollPane);
    }

    private void realizarCompra() {
        // Obtener datos ingresados en la interfaz
        String numeroTarjeta = numeroTarjetaTxt.getText().trim();
        String cvv = cvvTxt.getText().trim();
        String fechaVencimiento = fechaTxt.getText().trim();
        String productoSeleccionado = (String) productoCombo.getSelectedItem();

        // Extraer nombre del producto y precio
        String[] partes = productoSeleccionado.split(" - ");
        String producto = partes[0]; // Nombre del producto (Netflix, Disney+, etc.)
        double monto = Double.parseDouble(partes[1].replace("$", "").replace(".", "").trim()); // Extraer el monto

        // Validar que los campos no estén vacíos
        if (numeroTarjeta.isEmpty() || cvv.isEmpty() || fechaVencimiento.isEmpty()) {
            mensajesTxt.setText("⚠️ Todos los campos son obligatorios.");
            return;
        }

        // **Aquí agregamos la identificación del cliente**
        String identificacionCliente = JOptionPane.showInputDialog(this, "Ingrese su identificación:", "Validación", JOptionPane.QUESTION_MESSAGE);
        if (identificacionCliente == null || identificacionCliente.trim().isEmpty()) {
            mensajesTxt.setText("⚠️ Debe ingresar una identificación válida.");
            return;
        }

        // Crear la solicitud de compra
        RequestDTO request = new RequestDTO(
                "COMPRA_PRODUCTO",
                identificacionCliente, // ✅ Se incluye la identificación
                numeroTarjeta,
                null, null, // Fecha desde/hasta no aplican
                monto,
                cvv,
                fechaVencimiento,
                producto
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
                mensajesTxt.setText("✅ Compra realizada con éxito.\nNuevo cupo disponible: " + response.getCupoDisponible());
            } else {
                mensajesTxt.setText("❌ Error: " + response.getMensaje());
            }
        } catch (IOException e) {
            mensajesTxt.setText("❌ Error de conexión con el servidor.");
        }
    }
}
