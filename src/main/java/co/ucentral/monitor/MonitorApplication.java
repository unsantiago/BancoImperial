package co.ucentral.monitor;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class MonitorApplication {

    private static Process servidorProceso = null;

    public static void main(String[] args) {
        servidorProceso = iniciarServidor();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> detenerServidor()));
        SpringApplication.run(MonitorApplication.class, args);
    }

    private static Process iniciarServidor() {
        try {
            String classpath = "target/classes;target/dependency/*"; // Windows
            if (System.getProperty("os.name").toLowerCase().contains("linux") ||
                    System.getProperty("os.name").toLowerCase().contains("mac")) {
                classpath = "target/classes:target/dependency/*"; // Linux/macOS
            }

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", classpath, "co.ucentral.server.ServidorTCP");
            processBuilder.inheritIO();
            return processBuilder.start();
        } catch (IOException e) {
            System.err.println("❌ Error al iniciar el servidor: " + e.getMessage());
            return null;
        }
    }

    @PreDestroy
    public static void detenerServidor() {
        if (servidorProceso != null && servidorProceso.isAlive()) {
            System.out.println("🛑 Deteniendo servidor TCP...");
            servidorProceso.destroy();
            try {
                servidorProceso.waitFor(); // Espera a que termine
                System.out.println("✅ Servidor TCP detenido.");
            } catch (InterruptedException e) {
                System.err.println("❌ Error al detener el servidor: " + e.getMessage());
            }
        }
    }
}
