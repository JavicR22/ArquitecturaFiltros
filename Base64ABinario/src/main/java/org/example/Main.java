package org.example;

import org.example.processor.Processor;
import org.example.processor.ProcessorFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
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
                Path inputPath = Paths.get(filePath);

                // Validar que el archivo existe y es válido
                if (!Files.exists(inputPath)) {
                    System.err.println("Error: El archivo '" + filePath + "' no existe.");
                    System.exit(1);
                }
                if (!Files.isReadable(inputPath)) {
                    System.err.println("Error: No se puede leer el archivo '" + filePath + "'. Verifique los permisos.");
                    System.exit(1);
                }
                if (Files.isDirectory(inputPath)) {
                    System.err.println("Error: '" + filePath + "' es un directorio, no un archivo.");
                    System.exit(1);
                }
                if (Files.size(inputPath) == 0) {
                    System.err.println("Error: El archivo '" + filePath + "' está vacío.");
                    System.exit(1);
                }

                // Procesar el archivo
                Processor processor = ProcessorFactory.createFileProcessor(filePath);
                processor.process();

                System.out.println(processor.getOutputPath().toAbsolutePath().toString());

            } else if (args.length > 0) {
                String filePath = args[0];
                Path inputPath = Paths.get(filePath);

                if (!Files.exists(inputPath)) {
                    System.err.println("Error: El archivo '" + filePath + "' no existe.");
                    System.exit(1);
                }
                if (!Files.isReadable(inputPath)) {
                    System.err.println("Error: No se puede leer el archivo '" + filePath + "'. Verifique los permisos.");
                    System.exit(1);
                }
                if (Files.isDirectory(inputPath)) {
                    System.err.println("Error: '" + filePath + "' es un directorio, no un archivo.");
                    System.exit(1);
                }
                if (Files.size(inputPath) == 0) {
                    System.err.println("Error: El archivo '" + filePath + "' está vacío.");
                    System.exit(1);
                }

                Processor processor = ProcessorFactory.createFileProcessor(filePath);
                processor.process();

                System.out.println(processor.getOutputPath().toAbsolutePath().toString());

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
