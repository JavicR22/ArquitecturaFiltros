package org.example.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OutputWriter {
    public void writeToFile(String filePath, String output) throws IOException {
        Files.writeString(Path.of(filePath), output);
    }

    public void writeToStdout(String output) {

        System.out.println(output);
    }
}
