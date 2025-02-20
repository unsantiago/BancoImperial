package co.ucentral.server;


import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorTCP {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private final List<ClientHandler> clientesConectados;

    public ServidorTCP() {
        clientesConectados = new ArrayList<>();
    }

    public void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor TCP en ejecución en el puerto " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // Crear un nuevo manejador de clientes
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientesConectados.add(clientHandler);
                clientHandler.start(); // Iniciar el hilo para manejar la comunicación con el cliente
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServidorTCP servidor = new ServidorTCP();
        servidor.iniciarServidor();
    }
}

