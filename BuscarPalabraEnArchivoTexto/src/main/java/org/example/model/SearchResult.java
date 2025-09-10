package org.example.model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SearchResult {
    private final String searchWord;
    private final LocalDateTime timestamp;
    private final InputMode mode;
    private final Map<Path, FileSearchResult> fileResults;
    private final int totalOccurrences;
    private final int filesProcessed;
    private final int filesWithMatches;

    public SearchResult(String searchWord, LocalDateTime timestamp, InputMode mode,
                        Map<Path, FileSearchResult> fileResults, int totalOccurrences,
                        int filesProcessed, int filesWithMatches) {
        this.searchWord = searchWord;
        this.timestamp = timestamp;
        this.mode = mode;
        this.fileResults = fileResults;
        this.totalOccurrences = totalOccurrences;
        this.filesProcessed = filesProcessed;
        this.filesWithMatches = filesWithMatches;
    }

    public String getSearchWord() { return searchWord; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public InputMode getMode() { return mode; }
    public Map<Path, FileSearchResult> getFileResults() { return fileResults; }
    public int getTotalOccurrences() { return totalOccurrences; }
    public int getFilesProcessed() { return filesProcessed; }
    public int getFilesWithMatches() { return filesWithMatches; }

    public static class FileSearchResult {
        private final int occurrences;
        private final List<Integer> lineNumbers;
        private final long fileSizeBytes;

        public FileSearchResult(int occurrences, List<Integer> lineNumbers, long fileSizeBytes) {
            this.occurrences = occurrences;
            this.lineNumbers = lineNumbers;
            this.fileSizeBytes = fileSizeBytes;
        }

        public int getOccurrences() { return occurrences; }
        public List<Integer> getLineNumbers() { return lineNumbers; }
        public long getFileSizeBytes() { return fileSizeBytes; }
    }
}
