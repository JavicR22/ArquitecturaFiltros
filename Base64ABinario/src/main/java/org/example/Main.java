package org.example;

import org.example.processor.Processor;
import org.example.processor.ProcessorFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0 && System.in.available() > 0) {
                Processor processor = ProcessorFactory.createPipelineProcessor();
                processor.process();
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

                System.err.println("Conversión completada exitosamente:");
                System.err.println("Archivo origen: " + inputPath.toAbsolutePath());
                System.err.println("Archivo destino: " + processor.getOutputPath().toAbsolutePath());
            } else {
                System.err.println("Error: Debe proporcionar un archivo de entrada o datos por stdin.");
                System.err.println("Uso:");
                System.err.println("  Archivo: java -jar base64decoder.jar <archivo>");
                System.err.println("  Pipeline: cat archivo.txt | java -jar base64decoder.jar");
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
