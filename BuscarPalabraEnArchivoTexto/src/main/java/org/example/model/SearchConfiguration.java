package org.example.model;

import java.nio.file.Path;

public class SearchConfiguration {
    private final InputMode mode;
    private final String searchWord;
    private final Path outputPath;
    private final boolean caseSensitive;
    private final Path inputPath;

    public SearchConfiguration(InputMode mode, String searchWord, Path outputPath, boolean caseSensitive, Path inputPath) {
        this.mode = mode;
        this.searchWord = searchWord;
        this.outputPath = outputPath;
        this.caseSensitive = caseSensitive;
        this.inputPath = inputPath;
    }

    public InputMode getMode() { return mode; }
    public String getSearchWord() { return searchWord; }
    public Path getOutputPath() { return outputPath; }
    public boolean isCaseSensitive() { return caseSensitive; }
    public Path getInputPath() { return inputPath; }
}
