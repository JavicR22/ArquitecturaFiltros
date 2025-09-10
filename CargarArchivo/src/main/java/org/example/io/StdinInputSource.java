package org.example.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StdinInputSource implements InputSource {
    private String filePath;

    @Override
    public String readContent() throws IOException {
        if (System.in.available() == 0) {
            throw new IllegalArgumentException("No hay datos disponibles en stdin");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            filePath = reader.readLine();
            if (filePath == null || filePath.trim().isEmpty()) {
                throw new IllegalArgumentException("No se recibió una ruta de archivo válida desde stdin");
            }
            filePath = filePath.trim();
        }

        return filePath;
    }

    @Override
    public void close() throws IOException {
        // No cleanup needed for path-based approach
    }

    public Path getFilePath() {
        if (filePath == null) {
            throw new IllegalStateException("No se ha leído ninguna ruta de archivo");
        }
        return Paths.get(filePath);
    }

    public void cleanup() throws IOException {
        close();
    }
}
