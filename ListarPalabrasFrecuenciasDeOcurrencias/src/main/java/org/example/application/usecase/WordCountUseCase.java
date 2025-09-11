package org.example.application.usecase;

import org.example.domain.entities.DirectoryWordCountResult;
import org.example.domain.entities.WordCountResult;


import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCountUseCase {
    private final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public WordCountResult countSpecificWordInFile(String filePath, String word) throws IOException {
        String content = Files.readString(Paths.get(filePath));
        int count = countWordOccurrences(content, word);
        return new WordCountResult(filePath, Map.of(word, count));
    }

    public DirectoryWordCountResult countSpecificWordInDirectory(String dirPath, String word)
            throws IOException, InterruptedException {
        DirectoryWordCountResult dirResult = new DirectoryWordCountResult(dirPath);

        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            List<Future<WordCountResult>> futures = stream
                    .filter(Files::isRegularFile) // solo archivos
                    .map(path -> executor.submit(() -> {
                        String content = Files.readString(path);
                        int count = countWordOccurrences(content, word);
                        return new WordCountResult(path.toString(), Map.of(word, count));
                    }))
                    .collect(Collectors.toList());

            for (Future<WordCountResult> future : futures) {
                try {
                    dirResult.addFileResult(future.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        return dirResult;
    }

    public WordCountResult countSpecificWordFromString(String content, String source, String word) {
        int count = countWordOccurrences(content, word);
        return new WordCountResult(source, Map.of(word, count));
    }

    private int countWordOccurrences(String text, String word) {
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public void shutdown() {
        executor.shutdown();
    }

}
