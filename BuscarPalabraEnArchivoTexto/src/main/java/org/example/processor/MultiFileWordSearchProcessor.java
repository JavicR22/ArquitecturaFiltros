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
import java.util.concurrent.*;

public class MultiFileWordSearchProcessor implements WordSearchProcessor {

    @Override
    public SearchResult search(String word, List<Path> inputFiles, boolean caseSensitive) throws IOException {
        Map<Path, SearchResult.FileSearchResult> fileResults = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<Void>> futures = new ArrayList<>();

        String searchWord = caseSensitive ? word : word.toLowerCase();

        for (Path file : inputFiles) {
            Future<Void> future = executor.submit(() -> {
                try {
                    processFile(file, searchWord, caseSensitive, fileResults);
                } catch (IOException e) {
                    throw new RuntimeException("Error procesando archivo: " + file, e);
                }
                return null;
            });
            futures.add(future);
        }

        // Wait for all threads to complete
        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException("Error en procesamiento paralelo: " + e.getMessage(), e);
        } finally {
            executor.shutdown();
        }

        // Calculate totals
        int totalOccurrences = fileResults.values().stream()
                .mapToInt(SearchResult.FileSearchResult::getOccurrences)
                .sum();

        int filesWithMatches = (int) fileResults.values().stream()
                .filter(result -> result.getOccurrences() > 0)
                .count();

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

    private void processFile(Path file, String searchWord, boolean caseSensitive,
                             Map<Path, SearchResult.FileSearchResult> fileResults) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Integer> matchingLines = new ArrayList<>();
        int fileOccurrences = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = caseSensitive ? lines.get(i) : lines.get(i).toLowerCase();
            int lineOccurrences = countOccurrences(line, searchWord);

            if (lineOccurrences > 0) {
                matchingLines.add(i + 1);
                fileOccurrences += lineOccurrences;
            }
        }

        long fileSize = Files.size(file);
        fileResults.put(file, new SearchResult.FileSearchResult(fileOccurrences, matchingLines, fileSize));
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
