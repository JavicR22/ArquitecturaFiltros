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
    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public long getInputSize() {
        return inputSize;
    }

    public long getOutputSize() {
        return outputSize;
    }

    public LocalDateTime getConversionTime() {
        return conversionTime;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Métodos de utilidad adicionales
    public boolean hasError() {
        return !successful;
    }

    public String getInputFileName() {
        if (inputPath == null) return null;
        return java.nio.file.Paths.get(inputPath).getFileName().toString();
    }

    public String getOutputFileName() {
        if (outputPath == null) return null;
        return java.nio.file.Paths.get(outputPath).getFileName().toString();
    }

    public double getExpansionRatio() {
        if (inputSize == 0) return 0.0;
        return (double) outputSize / inputSize;
    }

    public long getSizeDifference() {
        return outputSize - inputSize;
    }

    @Override
    public String toString() {
        return "FileConversionResult{" +
                "inputPath='" + inputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", inputSize=" + inputSize +
                ", outputSize=" + outputSize +
                ", conversionTime=" + conversionTime +
                ", successful=" + successful +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileConversionResult that = (FileConversionResult) o;

        if (inputSize != that.inputSize) return false;
        if (outputSize != that.outputSize) return false;
        if (successful != that.successful) return false;
        if (inputPath != null ? !inputPath.equals(that.inputPath) : that.inputPath != null) return false;
        if (outputPath != null ? !outputPath.equals(that.outputPath) : that.outputPath != null) return false;
        if (conversionTime != null ? !conversionTime.equals(that.conversionTime) : that.conversionTime != null) return false;
        return errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null;
    }

    @Override
    public int hashCode() {
        int result = inputPath != null ? inputPath.hashCode() : 0;
        result = 31 * result + (outputPath != null ? outputPath.hashCode() : 0);
        result = 31 * result + (int) (inputSize ^ (inputSize >>> 32));
        result = 31 * result + (int) (outputSize ^ (outputSize >>> 32));
        result = 31 * result + (conversionTime != null ? conversionTime.hashCode() : 0);
        result = 31 * result + (successful ? 1 : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }
}
