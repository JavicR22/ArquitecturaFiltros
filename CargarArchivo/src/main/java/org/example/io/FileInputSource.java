package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class FileInputSource implements InputSource {
    private final Path filePath;

    public FileInputSource(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public String readContent() throws IOException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws IOException {
    }
}
