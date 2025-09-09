package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOutputSink implements OutputSink {
    private final Path outputPath;

    public FileOutputSink(Path outputPath) {
        if (outputPath == null) {
            throw new IllegalArgumentException("La ruta de salida no puede ser null.");
        }
        this.outputPath = outputPath;
    }

    @Override
    public void writeContent(byte[] content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("El contenido a escribir no puede ser null.");
        }

        try {
            Files.write(outputPath, content);
        } catch (IOException e) {
            throw new IOException("Error al escribir el archivo '" + outputPath + "': " + e.getMessage(), e);
        }
    }

    @Override
    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public void close() throws IOException {
    }
}
