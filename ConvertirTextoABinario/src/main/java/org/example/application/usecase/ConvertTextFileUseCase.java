package org.example.application.usecase;

import org.example.application.port.FileConverter;

import java.io.File;

public class ConvertTextFileUseCase {
    private final FileConverter fileConverter;

    public ConvertTextFileUseCase(FileConverter fileConverter) {
        this.fileConverter = fileConverter;
    }

    public void execute(String inputPath) {
        File input = new File(inputPath);

        if (!input.exists()) {
            throw new IllegalArgumentException("El archivo no existe: " + inputPath);
        }

        String outputPath = input.getAbsolutePath() + ".bin";
        File output = new File(outputPath);

        fileConverter.convert(input, output);
        System.out.println("Conversión completada: " + outputPath);
    }
}
