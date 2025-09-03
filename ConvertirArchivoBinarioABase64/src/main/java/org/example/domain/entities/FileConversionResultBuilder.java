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

    private FileConversionResultBuilder() {}

    public static FileConversionResultBuilder create() {
        return new FileConversionResultBuilder();
    }

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
        return new FileConversionResult(
                inputPath,
                outputPath,
                inputSize,
                outputSize,
                conversionTime,
                successful,
                errorMessage
        );
    }
}
