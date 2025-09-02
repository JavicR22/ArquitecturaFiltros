package org.example.domain.ports;
import java.io.IOException;

public interface FileReaderPort {
    byte[] readFile(String filePath) throws IOException;
    boolean fileExists(String filePath);
    long getFileSize(String filePath);
}
