package org.example;

import org.example.factory.ProcessorFactory;
import org.example.processor.FiltroImagenProcess;
import org.example.processor.ImageProcessor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Clase principal refactorizada para soportar múltiples modos de operación.
 * Implementa el principio de Responsabilidad Única (SRP).
 */
public class Main {
    public static void main(String[] args) {
        try {


            if (args.length == 0) {
                System.err.println("Modo pipeline: leyendo ruta de directorio desde stdin...");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String directoryPath = reader.readLine();
                FiltroImagenProcess filtroImagenProcess= new FiltroImagenProcess();
                filtroImagenProcess.process(directoryPath);


            } else if (args.length == 1) {

                // Modo archivo: procesar carpeta
                System.out.println();
                System.out.flush();
                System.err.println("Modo archivo: procesando carpeta " + args[0]);
                FiltroImagenProcess filtroImagenProcess= new FiltroImagenProcess();
                filtroImagenProcess.process(args[0]);

            } else {
                mostrarUso();
                return;
            }


        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void mostrarUso() {
        System.err.println("Uso:");
        System.err.println("  Modo archivo:   java -jar filtros.jar <ruta_carpeta>");
        System.err.println("  Modo pipeline:  echo '/ruta/directorio' | java -jar filtros.jar");
        System.err.println("  Pipeline:       echo '/ruta/directorio' | java -jar filtros.jar");
        System.err.println();
        System.err.println("Formatos soportados: PNG, JPG, JPEG, BMP, GIF, TIFF, WEBP");
        System.err.println();
        System.err.println("El programa procesará recursivamente todos los subdirectorios");
        System.err.println("y creará la estructura de salida en '<directorio>/imagenesConFiltros'");
    }
}
