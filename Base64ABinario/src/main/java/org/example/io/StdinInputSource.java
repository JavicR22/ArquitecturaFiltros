package org.example.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StdinInputSource implements InputSource {
    private final BufferedReader reader;

    public StdinInputSource() {
        this.reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }

    @Override
    public String readContent() throws IOException {
        StringBuilder content = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IOException("Error al leer desde stdin: " + e.getMessage(), e);
        }

        String result = content.toString().trim();
        if (result.isEmpty()) {
            throw new IOException("No se recibieron datos desde stdin.");
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            reader.close();
        } catch (IOException e) {
            throw new IOException("Error al cerrar stdin: " + e.getMessage(), e);
        }
    }
}
