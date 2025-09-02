package org.example.infrastructure.adapters;

import org.example.domain.ports.FileReaderPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileReader implements FileReaderPort {

    @Override
    public byte[] readFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return Files.readAllBytes(path);
}

    @Override
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    @Override
    public long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return 0L;
        }
    }
}
