package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class FileInputSource implements InputSource {
    private final Path filePath;

    public FileInputSource(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("La ruta del archivo no puede ser null.");
        }
        this.filePath = filePath;
    }

    @Override
    public String readContent() throws IOException {
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            return new String(fileBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo '" + filePath + "': " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
    }
}
