package org.example.application.usecase;

import org.example.application.port.FileConverter;
import org.example.infrastructure.TextToBinaryFileConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConvertTextFileUseCase {
    private final TextToBinaryFileConverter converter;

    public ConvertTextFileUseCase(TextToBinaryFileConverter converter) {
        this.converter = converter;
    }

    // Entrada desde archivo → genera archivo de salida
    public void execute(String inputPath) throws IOException {
        Path inputFile = Paths.get(inputPath);
        String outputFile = buildOutputFileName(inputFile);

        try (InputStream in = Files.newInputStream(inputFile)) {
            converter.convertToFile(in, outputFile);
        }
        System.out.println("✅ Archivo binario generado: " + outputFile);
    }

    // Entrada desde stdin → salida por stdout
    public void execute(InputStream in, String originalFile) throws IOException {
        String outputFileName = buildOutputFileName(Paths.get(originalFile));
        converter.convertToStdout(in, outputFileName);
    }

    private String buildOutputFileName(Path inputFile) {
        String name = inputFile.getFileName().toString();
        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : name.substring(dotIndex);

        Path parent = inputFile.getParent();
        if (parent == null) {
            parent = Paths.get("."); // ✅ directorio actual
        }

        return parent.resolve(baseName + "Binario" + extension).toString();
    }
}
