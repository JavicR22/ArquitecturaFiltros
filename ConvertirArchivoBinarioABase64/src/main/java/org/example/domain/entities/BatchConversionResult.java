package org.example.domain.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class BatchConversionResult {
    private final String directoryPath;
    private final List<FileConversionResult> results;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int totalFiles;
    private final int successfulConversions;
    private final int failedConversions;

    public BatchConversionResult(String directoryPath,
                                 List<FileConversionResult> results,
                                 LocalDateTime startTime,
                                 LocalDateTime endTime,
                                 int totalFiles,
                                 int successfulConversions,
                                 int failedConversions) {
        this.directoryPath = directoryPath;
        this.results = results;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalFiles = totalFiles;
        this.successfulConversions = successfulConversions;
        this.failedConversions = failedConversions;
    }

    // Getters
    public String getDirectoryPath() { return directoryPath; }
    public List<FileConversionResult> getResults() { return results; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getTotalFiles() { return totalFiles; }
    public int getSuccessfulConversions() { return successfulConversions; }
    public int getFailedConversions() { return failedConversions; }

    public boolean hasFailures() {
        return failedConversions > 0;
    }

    public long getTotalProcessingTimeMillis() {
        return java.time.Duration.between(startTime, endTime).toMillis();
    }
}
