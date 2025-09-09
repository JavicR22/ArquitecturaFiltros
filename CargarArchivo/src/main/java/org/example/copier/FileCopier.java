package org.example.copier;

import org.example.util.FileUtils;

import java.io.IOException;
import java.nio.file.*;

public class FileCopier {

    public Path copy(Path origen, Path directorioDestino) throws IOException {
        if (!Files.exists(directorioDestino) || !Files.isDirectory(directorioDestino)) {
            throw new IllegalArgumentException("El directorio de destino no existe: " + directorioDestino);
        }

        if (!Files.isWritable(directorioDestino)) {
            throw new IllegalArgumentException("No hay permisos de escritura en el directorio de destino: " + directorioDestino);
        }

        Path destino = directorioDestino.resolve(origen.getFileName());
        destino = FileUtils.resolveConflict(destino);

        return Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
    }
}
