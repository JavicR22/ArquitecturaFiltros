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
        if (System.console() == null) {
            return true;
        }

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

        // Check extension first
        if (!TEXT_EXTENSIONS.stream().anyMatch(fileName::endsWith)) {
            return false;
        }

        // Additional check for binary content
        try {
            if (!Files.isRegularFile(file) || !Files.isReadable(file)) {
                return false;
            }

            // Read first 1024 bytes to check for binary content
            byte[] sample = Files.readAllBytes(file);
            if (sample.length > 1024) {
                sample = Arrays.copyOf(sample, 1024);
            }

            // Check for null bytes (common in binary files)
            for (byte b : sample) {
                if (b == 0) {
                    return false;
                }
            }

            // Try to decode as UTF-8
            return validateUtf8Content(sample);
        } catch (IOException e) {
            return false;
        }
    }

    public static String readDirectoryPathFromStdin(InputStream stdin) throws IOException {
        byte[] pathBytes = stdin.readAllBytes();
        String path = new String(pathBytes, StandardCharsets.UTF_8).trim();

        if (path.isEmpty()) {
            throw new IOException("No se recibió ruta de directorio desde stdin");
        }

        return path;
    }

    public static Path createTempFileFromStdin(InputStream stdin) throws IOException {
        Path tempFile = Files.createTempFile("stdin_input", ".txt");
        Files.copy(stdin, tempFile, StandardCopyOption.REPLACE_EXISTING);

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
