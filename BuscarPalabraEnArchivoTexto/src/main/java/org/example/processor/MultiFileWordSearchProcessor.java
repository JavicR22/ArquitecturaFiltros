package org.example.processor;

import org.example.model.InputMode;
import org.example.model.SearchResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiFileWordSearchProcessor implements WordSearchProcessor {

    @Override
    public SearchResult search(String word, List<Path> inputFiles, boolean caseSensitive) throws IOException {
        Map<Path, SearchResult.FileSearchResult> fileResults = new HashMap<>();
        int totalOccurrences = 0;
        int filesWithMatches = 0;

        String searchWord = caseSensitive ? word : word.toLowerCase();

        for (Path file : inputFiles) {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            List<Integer> matchingLines = new ArrayList<>();
            int fileOccurrences = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = caseSensitive ? lines.get(i) : lines.get(i).toLowerCase();
                int lineOccurrences = countOccurrences(line, searchWord);
                
                if (lineOccurrences > 0) {
                    matchingLines.add(i + 1); // Line numbers start at 1
                    fileOccurrences += lineOccurrences;
                }
            }

            long fileSize = Files.size(file);
            fileResults.put(file, new SearchResult.FileSearchResult(fileOccurrences, matchingLines, fileSize));
            
            totalOccurrences += fileOccurrences;
            if (fileOccurrences > 0) {
                filesWithMatches++;
            }
        }

        return new SearchResult(
            word,
            LocalDateTime.now(),
            InputMode.DIRECTORY,
            fileResults,
            totalOccurrences,
            inputFiles.size(),
            filesWithMatches
        );
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        
        return count;
    }
}
