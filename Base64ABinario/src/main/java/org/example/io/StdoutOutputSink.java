package org.example.io;

import java.io.IOException;
import java.nio.file.Path;

public class StdoutOutputSink implements OutputSink {

    @Override
    public void writeContent(byte[] content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("El contenido a escribir no puede ser null.");
        }

        try {
            System.out.write(content);
            System.out.flush();
        } catch (IOException e) {
            throw new IOException("Error al escribir a stdout: " + e.getMessage(), e);
        }
    }

    @Override
    public Path getOutputPath() {
        return null;
    }

    @Override
    public void close() throws IOException {
    }
}
