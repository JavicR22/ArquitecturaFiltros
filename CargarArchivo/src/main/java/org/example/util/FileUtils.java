package org.example.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static Path resolveConflict(Path destino) {
        int contador = 1;
        Path nuevoDestino = destino;

        while (Files.exists(nuevoDestino)) {
            String nombre = destino.getFileName().toString();
            String baseName = nombre.contains(".")
                    ? nombre.substring(0, nombre.lastIndexOf('.'))
                    : nombre;
            String extension = nombre.contains(".")
                    ? nombre.substring(nombre.lastIndexOf('.'))
                    : "";

            nuevoDestino = destino.getParent().resolve(baseName + "(" + contador + ")" + extension);
            contador++;
        }

        return nuevoDestino;
    }
}
