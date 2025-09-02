package org.example.domain.entities;
import java.time.LocalDateTime;


public class FileConversionResult {
    private final String inputPath;
    private final String outputPath;
    private final long inputSize;
    private final long outputSize;
    private final LocalDateTime conversionTime;
    private final boolean successful;
    private final String errorMessage;

    public FileConversionResult(String inputPath, String outputPath, long inputSize,
                                long outputSize, LocalDateTime conversionTime,
                                boolean successful, String errorMessage) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.conversionTime = conversionTime;
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getInputPath() { return inputPath; }
    public String getOutputPath() { return outputPath; }
    public long getInputSize() { return inputSize; }
    public long getOutputSize() { return outputSize; }
    public LocalDateTime getConversionTime() { return conversionTime; }
    public boolean isSuccessful() { return successful; }
    public String getErrorMessage() { return errorMessage; }
}
