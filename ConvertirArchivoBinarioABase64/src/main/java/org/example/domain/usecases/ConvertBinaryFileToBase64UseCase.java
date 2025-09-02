package org.example.domain.usecases;

import org.example.domain.entities.FileConversionResult;
import org.example.domain.entities.FileConversionResultBuilder;
import org.example.domain.ports.BinaryToBase64ConverterPort;
import org.example.domain.ports.FileReaderPort;
import org.example.domain.ports.FileWriterPort;

import java.time.LocalDateTime;


public class ConvertBinaryFileToBase64UseCase {
    private final FileReaderPort fileReader;
    private final FileWriterPort fileWriter;
    private final BinaryToBase64ConverterPort converter;

    public ConvertBinaryFileToBase64UseCase(
            FileReaderPort fileReader,
            FileWriterPort fileWriter,
            BinaryToBase64ConverterPort converter) {
        this.fileReader = fileReader;
        this.fileWriter = fileWriter;
        this.converter = converter;
    }

    public FileConversionResult execute(String inputPath) {
        FileConversionResultBuilder resultBuilder = FileConversionResultBuilder.create()
                .inputPath(inputPath)
                .conversionTime(LocalDateTime.now());

        try {
            validateInput(inputPath);

            byte[] binaryData = fileReader.readFile(inputPath);
            String base64Content = converter.convertToBase64(binaryData);

            String outputPath = fileWriter.generateOutputPath(inputPath, ".base64");
            fileWriter.writeFile(outputPath, base64Content);

            return resultBuilder
                    .outputPath(outputPath)
                    .inputSize(fileReader.getFileSize(inputPath))
                    .outputSize(base64Content.length())
                    .successful(true)
                    .build();

        } catch (Exception e) {
            return resultBuilder
                    .successful(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    private void validateInput(String inputPath) {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía");
        }

        if (!fileReader.fileExists(inputPath)) {
            throw new IllegalArgumentException("El archivo no existe: " + inputPath);
        }
    }
}
