package org.example.validator;

import org.example.model.InputMode;
import org.example.util.SearchUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class SearchInputValidator extends FileValidator {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".txt", ".log", ".csv", ".md", ".xml", ".json"
    );

    public boolean validateSearchWord(String word) throws ValidationException {
        if (word == null || word.trim().isEmpty()) {
            throw new ValidationException("La palabra de búsqueda no puede estar vacía");
        }
        return true;
    }

    public boolean validateDirectoryInput(Path directory) throws ValidationException {
        if (!Files.exists(directory)) {
            throw new ValidationException("La carpeta no existe: " + directory);
        }

        if (!Files.isDirectory(directory)) {
            throw new ValidationException("La ruta no es una carpeta: " + directory);
        }

        if (!Files.isReadable(directory)) {
            throw new ValidationException("Sin permisos de lectura en carpeta: " + directory);
        }

        try {
            List<Path> textFiles = SearchUtils.filterTextFiles(
                    Files.walk(directory, 1)
                            .filter(Files::isRegularFile)
                            .toList()
            );

            if (textFiles.isEmpty()) {
                throw new ValidationException("La carpeta no contiene archivos de texto válidos");
            }

            for (Path file : textFiles) {
                long size = Files.size(file);
                if (!isValidFileSize(size)) {
                    throw new ValidationException("Archivo demasiado grande: " + file + " (" + size + " bytes)");
                }
            }

        } catch (IOException e) {
            throw new ValidationException("Error al acceder a la carpeta: " + e.getMessage(), e);
        }

        return true;
    }

    public boolean validateStdinInput() throws ValidationException {
        // In pipelines, data might not be immediately available but will arrive
        // Let the actual read operation handle empty stdin gracefully
        return true;
    }

    public boolean validateOutputDirectory(Path outputPath) throws ValidationException {
        if (outputPath != null) {
            Path parent = outputPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                throw new ValidationException("El directorio de salida no existe: " + parent);
            }
            if (parent != null && !Files.isWritable(parent)) {
                throw new ValidationException("Sin permisos de escritura en: " + parent);
            }
        }
        return true;
    }

    @Override
    public boolean validate(Path path) throws ValidationException {
        return validateDirectoryInput(path);
    }

    public boolean validateConfiguration(InputMode mode, String searchWord, Path inputPath, Path outputPath) throws ValidationException {
        validateSearchWord(searchWord);
        validateOutputDirectory(outputPath);

        switch (mode) {
            case DIRECTORY -> validateDirectoryInput(inputPath);
            case STDIN -> validateStdinInput();
        }

        return true;
    }
}
