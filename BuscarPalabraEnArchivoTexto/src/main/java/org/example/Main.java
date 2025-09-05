package org.example;

import org.example.searcher.PipelineWordSearcher;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            mostrarUso();
            return;
        }

        try {
            PipelineWordSearcher searcher = new PipelineWordSearcher();
            searcher.detectAndSearch(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void mostrarUso() {
        System.out.println("Uso:");
        System.out.println("  Modo carpeta: java -jar BuscarPalabraEnArchivoTexto.jar \"palabra\" \"/ruta/carpeta\"");
        System.out.println("  Modo pipeline: componente_anterior | java -jar BuscarPalabraEnArchivoTexto.jar \"palabra\"");
        System.out.println();
        System.out.println("Ejemplos:");
        System.out.println("  java -jar BuscarPalabraEnArchivoTexto.jar \"error\" \"/var/log\"");
        System.out.println("  cat archivo.txt | java -jar BuscarPalabraEnArchivoTexto.jar \"TODO\"");
    }
}
