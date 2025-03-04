package co.ucentral.monitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Component
public class ServidorMonitor {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Process servidorProceso = null;

    @Scheduled(fixedRate = 15000) // Ejecuta cada 15 segundos
    public void verificarServidor() {
        if (!estaServidorActivo()) {
            System.out.println("⚠️ Servidor no está activo. Reiniciando...");
            reiniciarServidor();
        } else {
            System.out.println("✅ Servidor funcionando correctamente.");
        }
    }

    private boolean estaServidorActivo() {
        try (Socket socket = new Socket()) {
            socket.setReuseAddress(true); // Permite reutilizar el socket rápidamente
            socket.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT), 1000); // Timeout de conexión
            return true;
        } catch (IOException e) {
            return false; // No se pudo conectar, el servidor está caído
        }
    }



    private void reiniciarServidor() {
        try {
            // Si ya hay un proceso en ejecución, no lo volvemos a iniciar
            if (servidorProceso != null && servidorProceso.isAlive()) {
                System.out.println("⚠️ Ya hay un proceso de servidor en ejecución. No se iniciará otro.");
                return;
            }

            // Construir el classpath incluyendo todas las dependencias
            String classpath = "target/classes;target/dependency/*"; // En Windows
            if (System.getProperty("os.name").toLowerCase().contains("linux") ||
                    System.getProperty("os.name").toLowerCase().contains("mac")) {
                classpath = "target/classes:target/dependency/*"; // En Linux/macOS
            }

            // Ejecutar el servidor con todas las dependencias en el classpath
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", classpath, "co.ucentral.server.ServidorTCP");
            processBuilder.inheritIO(); // Redirigir la salida del servidor a la consola
            servidorProceso = processBuilder.start();

            System.out.println("🚀 Servidor reiniciado exitosamente.");
        } catch (IOException e) {
            System.err.println("❌ Error al intentar reiniciar el servidor: " + e.getMessage());
        }
    }

}
