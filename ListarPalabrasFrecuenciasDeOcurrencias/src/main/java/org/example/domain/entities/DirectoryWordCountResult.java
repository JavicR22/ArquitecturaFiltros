package org.example.domain.entities;

import java.util.*;

public class DirectoryWordCountResult {
    private final String directoryPath;
    private final List<WordCountResult> fileResults = new ArrayList<>();

    public DirectoryWordCountResult(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void addFileResult(WordCountResult result) {
        fileResults.add(result);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public List<WordCountResult> getFileResults() {
        return Collections.unmodifiableList(fileResults);
    }

    public int getTotalOccurrences() {
        return fileResults.stream()
                .mapToInt(WordCountResult::getTotalOccurrences)
                .sum();
    }
}
