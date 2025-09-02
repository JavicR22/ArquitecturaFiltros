package org.example.domain.entities;
import java.time.LocalDateTime;

public class FileConversionResultBuilder {
    private String inputPath;
    private String outputPath;
    private long inputSize;
    private long outputSize;
    private LocalDateTime conversionTime;
    private boolean successful;
    private String errorMessage;

    public FileConversionResultBuilder inputPath(String inputPath) {
        this.inputPath = inputPath;
        return this;
    }

    public FileConversionResultBuilder outputPath(String outputPath) {
        this.outputPath = outputPath;
        return this;
    }

    public FileConversionResultBuilder inputSize(long inputSize) {
        this.inputSize = inputSize;
        return this;
    }

    public FileConversionResultBuilder outputSize(long outputSize) {
        this.outputSize = outputSize;
        return this;
    }

    public FileConversionResultBuilder conversionTime(LocalDateTime conversionTime) {
        this.conversionTime = conversionTime;
        return this;
    }

    public FileConversionResultBuilder successful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public FileConversionResultBuilder errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public FileConversionResult build() {
        validateRequiredFields();
        return new FileConversionResult(inputPath, outputPath, inputSize,
                outputSize, conversionTime, successful, errorMessage);
    }

    private void validateRequiredFields() {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new IllegalStateException("InputPath es requerido");
        }
        if (conversionTime == null) {
            throw new IllegalStateException("ConversionTime es requerido");
        }
    }

    public static FileConversionResultBuilder create() {
        return new FileConversionResultBuilder();
    }
}
