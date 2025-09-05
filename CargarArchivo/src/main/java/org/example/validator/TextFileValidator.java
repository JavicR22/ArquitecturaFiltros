package org.example.validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class TextFileValidator implements FileValidator {

    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;
    private static final Set<String> VALID_EXTENSIONS = Set.of(".txt", ".csv", ".log", ".md", ".json", ".xml", ".html", ".css", ".js", ".java", ".py", ".sql");

    @Override
    public boolean isValid(Path file) {
        try {
            validateFile(file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void validateFile(Path file) throws IllegalArgumentException, IOException {
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("El archivo no existe: " + file.toAbsolutePath());
        }

        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("La ruta no corresponde a un archivo regular: " + file.toAbsolutePath());
        }

        if (!Files.isReadable(file)) {
            throw new IllegalArgumentException("El archivo no tiene permisos de lectura: " + file.toAbsolutePath());
        }

        long fileSize = Files.size(file);
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo es demasiado grande (máximo 100MB): " + (fileSize / (1024 * 1024)) + "MB");
        }

        String fileName = file.getFileName().toString().toLowerCase();
        boolean hasValidExtension = VALID_EXTENSIONS.stream()
                .anyMatch(fileName::endsWith);
        if (!hasValidExtension) {
            throw new IllegalArgumentException("Extensión de archivo no válida. Extensiones permitidas: " + VALID_EXTENSIONS);
        }

        byte[] bytes = Files.readAllBytes(file);
        for (byte b : bytes) {
            if (b < 0x09 && b != 0x0A && b != 0x0D) {
                throw new IllegalArgumentException("El archivo contiene caracteres binarios y no es un archivo de texto válido");
            }
        }
    }
}
