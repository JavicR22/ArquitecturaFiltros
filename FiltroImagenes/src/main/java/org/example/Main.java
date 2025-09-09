package org.example;

import org.example.factory.ProcessorFactory;
import org.example.processor.ImageProcessor;

/**
 * Clase principal refactorizada para soportar múltiples modos de operación.
 * Implementa el principio de Responsabilidad Única (SRP).
 */
public class Main {
    public static void main(String[] args) {
        try {
            ImageProcessor processor;

            if (args.length == 0) {
                // Modo pipeline: leer desde stdin, escribir a stdout
                System.err.println("Modo pipeline: leyendo desde stdin...");
                processor = ProcessorFactory.createPipelineProcessor();
            } else if (args.length == 1) {
                // Modo archivo: procesar carpeta
                System.err.println("Modo archivo: procesando carpeta " + args[0]);
                processor = ProcessorFactory.createFileProcessor(args[0]);
            } else {
                mostrarUso();
                return;
            }

            processor.procesarImagenes();

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void mostrarUso() {
        System.err.println("Uso:");
        System.err.println("  Modo archivo:   java -jar filtros.jar <ruta_carpeta>");
        System.err.println("  Modo pipeline:  cat imagen.jpg | java -jar filtros.jar");
        System.err.println("  Pipeline:       java -jar componente1.jar imagen.jpg | java -jar filtros.jar");
        System.err.println();
        System.err.println("Formatos soportados: PNG, JPG, JPEG, BMP, GIF, TIFF, WEBP");
    }
}
