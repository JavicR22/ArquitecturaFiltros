package org.example.infrastructure;

import org.example.domain.entities.DirectoryWordCountResult;
import org.example.domain.entities.WordCountResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class FileReportWriter {

    public void writeToStdout(WordCountResult result) {
        System.out.println("📄 Archivo: " + result.getFileName());
        result.getWordCounts().forEach((word, count) ->
                System.out.println("   '" + word + "' → " + count + " ocurrencias"));
        System.out.println("   Total: " + result.getTotalOccurrences());
    }

    public void writeDirectoryToStdout(DirectoryWordCountResult result) {
        System.out.println("📂 Directorio: " + result.getDirectoryPath());
        result.getFileResults().forEach(this::writeToStdout);
        System.out.println("📊 Total en directorio: " + result.getTotalOccurrences());
    }

    public void writeToFile(WordCountResult result, String outputPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("📄 Archivo: ").append(result.getFileName()).append("\n");
        result.getWordCounts().forEach((word, count) ->
                sb.append("   '").append(word).append("' → ").append(count).append(" ocurrencias\n"));
        sb.append("   Total: ").append(result.getTotalOccurrences()).append("\n");

        Files.writeString(Path.of(outputPath), sb.toString());
        System.out.println("✅ Reporte guardado en: " + outputPath);
    }

    public void writeDirectoryReport(DirectoryWordCountResult result, String outputPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("📂 Directorio: ").append(result.getDirectoryPath()).append("\n\n");

        for (WordCountResult fileResult : result.getFileResults()) {
            sb.append("Archivo: ").append(fileResult.getFileName()).append("\n");
            fileResult.getWordCounts().forEach((word, count) ->
                    sb.append("   '").append(word).append("' → ").append(count).append(" ocurrencias\n"));
            sb.append("   Total: ").append(fileResult.getTotalOccurrences()).append("\n\n");
        }

        sb.append("📊 Total en directorio: ").append(result.getTotalOccurrences()).append("\n");

        Files.writeString(Path.of(outputPath), sb.toString());
        System.out.println("✅ Reporte guardado en: " + outputPath);
    }
}
