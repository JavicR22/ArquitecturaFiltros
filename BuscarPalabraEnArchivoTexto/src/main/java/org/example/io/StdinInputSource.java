package org.example.io;

import org.example.util.SearchUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StdinInputSource implements InputSource {
    private Path tempFile;

    @Override
    public List<Path> getInputFiles() throws IOException {
        if (tempFile == null) {
            try {
                tempFile = SearchUtils.createTempFileFromStdin(System.in);

                // Check if the temp file has content
                if (Files.size(tempFile) == 0) {
                    Files.deleteIfExists(tempFile);
                    throw new IOException("No hay datos disponibles en stdin");
                }
            } catch (IOException e) {
                if (tempFile != null) {
                    Files.deleteIfExists(tempFile);
                }
                throw new IOException("Error al leer datos de stdin: " + e.getMessage(), e);
            }
        }
        return List.of(tempFile);
    }

    @Override
    public void cleanup() {
        if (tempFile != null) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                System.err.println("Advertencia: No se pudo eliminar archivo temporal: " + tempFile);
            }
        }
    }
}
