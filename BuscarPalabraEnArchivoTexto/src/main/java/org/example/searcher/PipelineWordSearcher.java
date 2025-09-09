package org.example.searcher;

import org.example.io.DirectoryInputSource;
import org.example.io.SearchResultOutputSink;
import org.example.io.StdinInputSource;
import org.example.processor.MultiFileWordSearchProcessor;
import org.example.processor.SingleFileWordSearchProcessor;
import org.example.validator.SearchInputValidator;
import org.example.validator.ValidationException;
import org.example.model.InputMode;

import java.io.IOException;
import java.nio.file.Path;

public class PipelineWordSearcher extends WordSearcher {
    private final SearchInputValidator validator;

    public PipelineWordSearcher() {
        super(null, null); // Will be set per operation
        this.validator = new SearchInputValidator();
    }

    @Override
    public void searchInDirectory(Path directory, String word, Path outputFile) throws IOException {
        try {
            validator.validateConfiguration(InputMode.DIRECTORY, word, directory, outputFile);
            
            var inputSource = new DirectoryInputSource(directory);
            var processor = new MultiFileWordSearchProcessor();
            var outputSink = new SearchResultOutputSink(outputFile);
            var searcher = new WordSearcher(processor, outputSink);
            
            searcher.search(word, inputSource, false); // Default case-insensitive
            
        } catch (ValidationException e) {
            throw new IOException("Error de validación: " + e.getMessage(), e);
        }
    }

    @Override
    public void searchFromStdin(String word, Path outputFile) throws IOException {
        try {
            validator.validateConfiguration(InputMode.STDIN, word, null, outputFile);
            
            var inputSource = new StdinInputSource();
            var processor = new SingleFileWordSearchProcessor();
            var outputSink = new SearchResultOutputSink(outputFile);
            var searcher = new WordSearcher(processor, outputSink);
            
            searcher.search(word, inputSource, false); // Default case-insensitive
            
        } catch (ValidationException e) {
            throw new IOException("Error de validación: " + e.getMessage(), e);
        }
    }

    public void detectAndSearch(String[] args) throws IOException {
        if (args.length == 1) {
            String word = args[0];
            Path outputFile = Path.of("search_results.txt");
            searchFromStdin(word, outputFile);
            System.out.println("Búsqueda completada. Resultados en: " + outputFile);
        } else if (args.length == 2) {
            String word = args[0];
            String directoryPath = args[1];
            Path outputFile = Path.of("search_results.txt");
            searchInDirectory(Path.of(directoryPath), word, outputFile);
            System.out.println("Búsqueda completada. Resultados en: " + outputFile);
        } else {
            throw new IOException("Argumentos inválidos");
        }
    }
}
