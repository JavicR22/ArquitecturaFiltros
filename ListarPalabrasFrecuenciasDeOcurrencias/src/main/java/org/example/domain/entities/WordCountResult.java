package org.example.domain.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCountResult {
    private final String fileName;
    private final Map<String, Integer> wordCounts;

    public WordCountResult(String fileName, Map<String, Integer> wordCounts) {
        this.fileName = fileName;
        this.wordCounts = wordCounts;
    }

    public String getFileName() {
        return fileName;
    }

    public Map<String, Integer> getWordCounts() {
        return Collections.unmodifiableMap(wordCounts);
    }

    public int getTotalOccurrences() {
        return wordCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
}
