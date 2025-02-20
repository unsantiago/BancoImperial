package co.ucentral.cliente.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import co.ucentral.dto.RequestDTO;
import co.ucentral.dto.ResponseDTO;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PrincipalCli extends javax.swing.JFrame {
    private final int PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean servidorActivo = false;
    private final Gson gson;

    private JTextField numeroTarjetaTxt;
    private JTextField identificacionTxt;  // Nuevo campo para la identificación
    private JTextArea mensajesTxt;
    private JButton btConsultarMovimientos;
    private JButton btConsultarCupo;
    private JButton bConectar;

    public PrincipalCli() {
        initComponents();
        this.bConectar.setEnabled(true);
        this.btConsultarMovimientos.setEnabled(false);
        this.btConsultarCupo.setEnabled(false);
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.setTitle("Cliente Banco Imperial");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        bConectar = new JButton("CONECTAR");
        bConectar.setFont(new java.awt.Font("Segoe UI", 0, 14));
        bConectar.addActionListener(evt -> conectar());
        getContentPane().add(bConectar);
        bConectar.setBounds(20, 20, 120, 30);

        JLabel jLabel1 = new JLabel("Cliente Banco Imperial");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(160, 20, 200, 17);

        mensajesTxt = new JTextArea();
        mensajesTxt.setColumns(20);
        mensajesTxt.setRows(5);
        mensajesTxt.setEnabled(false);
        JScrollPane jScrollPane1 = new JScrollPane(mensajesTxt);
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 250, 400, 100);

        JLabel jLabel2 = new JLabel("Número de Tarjeta:");
        jLabel2.setFont(new java.awt.Font("Verdana", 0, 14));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 60, 200, 20);

        numeroTarjetaTxt = new JTextField();
        getContentPane().add(numeroTarjetaTxt);
        numeroTarjetaTxt.setBounds(20, 80, 200, 30);

        JLabel jLabel3 = new JLabel("Identificación:");
        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 120, 200, 20);

        identificacionTxt = new JTextField();
        getContentPane().add(identificacionTxt);
        identificacionTxt.setBounds(20, 140, 200, 30);

        btConsultarMovimientos = new JButton("Consultar Movimientos");
        btConsultarMovimientos.setFont(new java.awt.Font("Verdana", 0, 14));
        btConsultarMovimientos.addActionListener(evt -> consultarMovimientos());
        getContentPane().add(btConsultarMovimientos);
        btConsultarMovimientos.setBounds(240, 80, 180, 30);

        btConsultarCupo = new JButton("Consultar Cupo");
        btConsultarCupo.setFont(new java.awt.Font("Verdana", 0, 14));
        btConsultarCupo.addActionListener(evt -> consultarCupoDisponible());
        getContentPane().add(btConsultarCupo);
        btConsultarCupo.setBounds(240, 140, 180, 30);

        setSize(new java.awt.Dimension(450, 400));
        setLocationRelativeTo(null);
    }

    private void conectar() {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket("localhost", PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                servidorActivo = true;
                mensajesTxt.append("Conectado al servidor.\n");

                this.bConectar.setEnabled(false);
                this.btConsultarMovimientos.setEnabled(true);
                this.btConsultarCupo.setEnabled(true);
            }
        } catch (IOException e) {
            mensajesTxt.append("No se pudo conectar con el servidor.\n");
            servidorActivo = false;
        }
    }

    private void consultarMovimientos() {
        if (!servidorActivo) {
            mensajesTxt.append("Debe conectarse al servidor primero.\n");
            return;
        }

        String numeroTarjeta = numeroTarjetaTxt.getText().trim();
        String identificacionCliente = identificacionTxt.getText().trim();

        if (numeroTarjeta.isEmpty() || identificacionCliente.isEmpty()) {
            mensajesTxt.append("Ingrese número de tarjeta e identificación.\n");
            return;
        }

        RequestDTO request = new RequestDTO("CONSULTA_MOVIMIENTOS", identificacionCliente, numeroTarjeta, null, null);
        enviarSolicitud(request);
    }

    private void consultarCupoDisponible() {
        if (!servidorActivo) {
            mensajesTxt.append("Debe conectarse al servidor primero.\n");
            return;
        }

        String numeroTarjeta = numeroTarjetaTxt.getText().trim();
        if (numeroTarjeta.isEmpty()) {
            mensajesTxt.append("Ingrese un número de tarjeta válido.\n");
            return;
        }

        RequestDTO request = new RequestDTO("CONSULTA_CUPO", "", numeroTarjeta, null, null);
        enviarSolicitud(request);
    }

    private void enviarSolicitud(RequestDTO request) {
        try {
            String jsonRequest = gson.toJson(request);
            out.println(jsonRequest);

            String jsonResponse = in.readLine();
            ResponseDTO response = gson.fromJson(jsonResponse, ResponseDTO.class);

            if ("OK".equals(response.getEstado())) {
                if (request.getTipo().equals("CONSULTA_MOVIMIENTOS")) {
                    mensajesTxt.append("Movimientos encontrados:\n");
                    for (var mov : response.getMovimientos()) {
                        mensajesTxt.append("Fecha: " + mov.getFecha() +
                                ", Valor: " + mov.getValor() +
                                ", Tipo: " + mov.getTipoMovimiento() +
                                ", Establecimiento: " + mov.getEstablecimiento() +
                                ", Terminal: " + mov.getTerminal() + "\n");
                    }
                } else if (request.getTipo().equals("CONSULTA_CUPO")) {
                    mensajesTxt.append("Cupo disponible: " + response.getCupoDisponible() + "\n");
                }
            } else {
                mensajesTxt.append("Error: " + response.getMensaje() + "\n");
            }
        } catch (IOException e) {
            mensajesTxt.append("Error en la comunicación con el servidor.\n");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new PrincipalCli().setVisible(true));
    }
}
