package org.example.io;

import org.example.util.SearchUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class StdinInputSource implements InputSource {
    private List<Path> inputFiles;

    @Override
    public List<Path> getInputFiles() throws IOException {
        if (inputFiles == null) {
            try {
                String directoryPath = SearchUtils.readDirectoryPathFromStdin(System.in);
                Path directory = Paths.get(directoryPath);

                if (!Files.exists(directory)) {
                    throw new IOException("El directorio no existe: " + directoryPath);
                }

                if (!Files.isDirectory(directory)) {
                    throw new IOException("La ruta no es un directorio: " + directoryPath);
                }

                try (Stream<Path> files = Files.list(directory)) {
                    List<Path> allFiles = files
                            .filter(Files::isRegularFile)
                            .toList();

                    inputFiles = SearchUtils.filterTextFiles(allFiles);

                    if (inputFiles.isEmpty()) {
                        throw new IOException("No se encontraron archivos de texto en el directorio: " + directoryPath);
                    }
                }
            } catch (IOException e) {
                throw new IOException("Error al procesar directorio desde stdin: " + e.getMessage(), e);
            }
        }
        return inputFiles;
    }

    @Override
    public void cleanup() {
        // No cleanup needed for directory-based input
    }
}
