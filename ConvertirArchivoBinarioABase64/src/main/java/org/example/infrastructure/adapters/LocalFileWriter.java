package org.example.infrastructure.adapters;

import org.example.domain.ports.FileWriterPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileWriter implements FileWriterPort {
    @Override
    public void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content.getBytes());
    }

    @Override
    public String generateOutputPath(String inputPath, String extension) {
        Path inputPathObj = Paths.get(inputPath);
        String fileName = inputPathObj.getFileName().toString();
        String nameWithoutExtension = getNameWithoutExtension(fileName);

        return inputPathObj.getParent()
                .resolve(nameWithoutExtension + extension)
                .toString();
    }

    private String getNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex != -1 ? fileName.substring(0, lastDotIndex) : fileName;
    }
}
