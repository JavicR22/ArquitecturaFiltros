package org.example.util;

import org.example.model.InputMode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class SearchUtils {
    private static final List<String> TEXT_EXTENSIONS = Arrays.asList(
            ".txt", ".log", ".csv", ".md", ".xml", ".json"
    );

    public static InputMode detectInputMode(String[] args) {
        if (args.length == 1 && isStdinAvailable()) {
            return InputMode.STDIN;
        } else if (args.length == 2) {
            return InputMode.DIRECTORY;
        }
        return null;
    }

    public static boolean isStdinAvailable() {
        // When System.console() is null, we're likely in a pipeline or redirect
        if (System.console() == null) {
            return true;
        }

        // Fallback check for available bytes
        try {
            return System.in.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<Path> filterTextFiles(List<Path> files) {
        return files.stream()
                .filter(SearchUtils::isTextFile)
                .toList();
    }

    private static boolean isTextFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return TEXT_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    public static Path createTempFileFromStdin(InputStream stdin) throws IOException {
        Path tempFile = Files.createTempFile("stdin_input", ".txt");
        Files.copy(stdin, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Validate UTF-8 content
        byte[] content = Files.readAllBytes(tempFile);
        if (!validateUtf8Content(content)) {
            Files.deleteIfExists(tempFile);
            throw new IOException("El contenido de stdin no es UTF-8 válido");
        }

        return tempFile;
    }

    public static boolean validateUtf8Content(byte[] content) {
        try {
            new String(content, StandardCharsets.UTF_8);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
