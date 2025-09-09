package org.example.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class StdinInputSource implements InputSource {
    private Path tempFile;

    @Override
    public String readContent() throws IOException {
        if (System.in.available() == 0) {
            throw new IllegalArgumentException("No hay datos disponibles en stdin");
        }

        tempFile = Files.createTempFile("stdin_input", ".txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }

        return Files.readString(tempFile, StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws IOException {
        if (tempFile != null && Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    public Path getTempFile() {
        return tempFile;
    }

    public void cleanup() throws IOException {
        close();
    }
}
