package org.example;

import org.example.util.Base64ABinarioProcess;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Base64ABinario {
    public static void main(String[] args) {
        Base64ABinarioProcess base64ABinarioProcess = new Base64ABinarioProcess();
        try {
            if (args.length == 0 && System.in.available() > 0) {
                // Leer la ruta del archivo desde stdin
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String filePath = reader.readLine();

                if (filePath == null || filePath.trim().isEmpty()) {
                    System.err.println("Error: No se proporcionó una ruta de archivo válida por stdin.");
                    System.exit(1);
                }

                filePath = filePath.trim();
                base64ABinarioProcess.process(filePath);
            } else if (args.length > 0) {
                String filePath = args[0];
                base64ABinarioProcess.process(filePath);

            } else {
                System.err.println("Error: Debe proporcionar un archivo de entrada o una ruta por stdin.");
                System.err.println("Uso:");
                System.err.println("  Archivo: java -jar Base64ABinario-1.0-SNAPSHOT.jar <archivo>");
                System.err.println("  Pipeline (ruta por stdin): echo \"/ruta/archivo.base64\" | java -jar Base64ABinario-1.0-SNAPSHOT.jar");
                System.exit(1);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error durante la conversión: " + e.getMessage());
            System.exit(1);
        }
    }
}
