package org.example.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputReader {
    public String readFromFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public String readFromStdin() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
