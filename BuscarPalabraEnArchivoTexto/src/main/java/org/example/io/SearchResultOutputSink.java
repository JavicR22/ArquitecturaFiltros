package org.example.io;

import org.example.model.InputMode;
import org.example.model.SearchResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

public class SearchResultOutputSink implements OutputSink {
    private final Path outputPath;
    private final boolean outputToStdout;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SearchResultOutputSink(Path outputPath) {
        this.outputPath = outputPath;
        this.outputToStdout = false;
    }

    public SearchResultOutputSink(Path outputPath, boolean outputToStdout) {
        this.outputPath = outputPath;
        this.outputToStdout = outputToStdout;
    }

    @Override
    public void writeResult(SearchResult result) throws IOException {
        StringBuilder content = new StringBuilder();

        if (result.getMode() == InputMode.DIRECTORY) {
            writeDirectoryResult(content, result);
        } else {
            writeStdinResult(content, result);
        }

        Files.writeString(outputPath, content.toString(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        if (outputToStdout) {
            System.out.print(content.toString());
        }
    }

    private void writeDirectoryResult(StringBuilder content, SearchResult result) {
        content.append("BÚSQUEDA EN CARPETA - Palabra: \"").append(result.getSearchWord()).append("\"\n");
        content.append("Carpeta: ").append(result.getFileResults().keySet().iterator().next().getParent()).append("\n");
        content.append("Fecha: ").append(result.getTimestamp().format(FORMATTER)).append("\n");
        content.append("========================================\n\n");

        result.getFileResults().forEach((path, fileResult) -> {
            content.append("Archivo: ").append(path.getFileName()).append(" (")
                    .append(formatFileSize(fileResult.getFileSizeBytes())).append(")\n");
            content.append("Ocurrencias: ").append(fileResult.getOccurrences()).append("\n");

            if (!fileResult.getLineNumbers().isEmpty()) {
                content.append("Líneas: ");
                content.append(String.join(", ", fileResult.getLineNumbers().stream()
                        .map(String::valueOf).toArray(String[]::new)));
                content.append("\n");
            }
            content.append("\n");
        });

        content.append("========================================\n");
        content.append("RESUMEN:\n");
        content.append("Archivos procesados: ").append(result.getFilesProcessed()).append("\n");
        content.append("Total ocurrencias: ").append(result.getTotalOccurrences()).append("\n");
        content.append("Archivos con coincidencias: ").append(result.getFilesWithMatches()).append("\n");
    }

    private void writeStdinResult(StringBuilder content, SearchResult result) {
        content.append("BÚSQUEDA EN PIPELINE - Palabra: \"").append(result.getSearchWord()).append("\"\n");
        content.append("Fuente: stdin (componente anterior)\n");
        content.append("Fecha: ").append(result.getTimestamp().format(FORMATTER)).append("\n");
        content.append("========================================\n\n");

        var fileResult = result.getFileResults().values().iterator().next();
        long totalLines = fileResult.getLineNumbers().isEmpty() ? 0 :
                fileResult.getLineNumbers().stream().mapToLong(Integer::longValue).max().orElse(0);

        content.append("Contenido procesado: ").append(totalLines).append(" líneas\n");
        content.append("Ocurrencias encontradas: ").append(fileResult.getOccurrences()).append("\n");

        if (!fileResult.getLineNumbers().isEmpty()) {
            content.append("Líneas: ");
            content.append(String.join(", ", fileResult.getLineNumbers().stream()
                    .map(String::valueOf).toArray(String[]::new)));
            content.append("\n");
        }

        content.append("\n========================================\n");
        content.append("RESUMEN:\n");
        content.append("Total ocurrencias: ").append(result.getTotalOccurrences()).append("\n");
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
}
