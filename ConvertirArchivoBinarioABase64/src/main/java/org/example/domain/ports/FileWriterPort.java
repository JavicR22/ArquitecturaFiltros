package org.example.domain.ports;

import java.io.IOException;

public interface FileWriterPort {
    void writeFile(String filePath, String content) throws IOException;
    String generateOutputPath(String inputPath, String extension);
}
