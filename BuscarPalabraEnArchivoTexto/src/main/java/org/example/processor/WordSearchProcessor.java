package org.example.processor;

import org.example.model.SearchResult;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface WordSearchProcessor {
    SearchResult search(String word, List<Path> inputFiles, boolean caseSensitive) throws IOException;
}
