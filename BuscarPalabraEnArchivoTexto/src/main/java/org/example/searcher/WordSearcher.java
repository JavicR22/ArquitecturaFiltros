package org.example.searcher;

import org.example.io.InputSource;
import org.example.io.OutputSink;
import org.example.model.SearchResult;
import org.example.processor.WordSearchProcessor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class WordSearcher {
    protected final WordSearchProcessor processor;
    protected final OutputSink outputSink;

    public WordSearcher(WordSearchProcessor processor, OutputSink outputSink) {
        this.processor = processor;
        this.outputSink = outputSink;
    }

    public void search(String word, InputSource inputSource, boolean caseSensitive) throws IOException {
        try {
            List<Path> inputFiles = inputSource.getInputFiles();
            SearchResult result = processor.search(word, inputFiles, caseSensitive);
            outputSink.writeResult(result);
        } finally {
            inputSource.cleanup();
        }
    }

    public void searchInDirectory(Path directory, String word, Path outputFile) throws IOException {
        // Implementation will be in PipelineWordSearcher
    }

    public void searchFromStdin(String word, Path outputFile) throws IOException {
        // Implementation will be in PipelineWordSearcher
    }
}
