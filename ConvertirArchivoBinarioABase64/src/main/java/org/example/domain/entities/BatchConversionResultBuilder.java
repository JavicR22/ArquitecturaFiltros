package org.example.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

public class BatchConversionResultBuilder {
    private String directoryPath;
    private List<FileConversionResult> results;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalFiles;
    private int successfulConversions;
    private int failedConversions;

    private BatchConversionResultBuilder() {}

    public static BatchConversionResultBuilder create() {
        return new BatchConversionResultBuilder();
    }

    public BatchConversionResultBuilder directoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public BatchConversionResultBuilder results(List<FileConversionResult> results) {
        this.results = results;
        return this;
    }

    public BatchConversionResultBuilder startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public BatchConversionResultBuilder endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public BatchConversionResultBuilder totalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
        return this;
    }

    public BatchConversionResultBuilder successfulConversions(int successfulConversions) {
        this.successfulConversions = successfulConversions;
        return this;
    }

    public BatchConversionResultBuilder failedConversions(int failedConversions) {
        this.failedConversions = failedConversions;
        return this;
    }

    public BatchConversionResult build() {
        return new BatchConversionResult(
                directoryPath,
                results,
                startTime,
                endTime,
                totalFiles,
                successfulConversions,
                failedConversions
        );
    }
}
