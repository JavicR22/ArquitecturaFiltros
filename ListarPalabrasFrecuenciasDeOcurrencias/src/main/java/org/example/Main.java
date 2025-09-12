package org.example;

import org.example.application.usecase.WordCountUseCase;
import org.example.domain.entities.DirectoryWordCountResult;
import org.example.domain.entities.WordCountResult;
import org.example.infrastructure.FileReportWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        WordCountUseCase useCase = new WordCountUseCase();
        FileReportWriter reportWriter = new FileReportWriter();

        try {
            if (args.length < 1) {
                System.err.println("Uso:");
                System.err.println("  • Directorio: java -jar EncontrarOcurrencias.jar <ruta_directorio> <palabra>");
                System.err.println("  • Archivo:    java -jar EncontrarOcurrencias.jar <ruta_archivo> <palabra>");
                System.err.println("  • Pipe:       java -jar EncontrarOcurrencias.jar <palabra> < archivo.txt");
                return;
            }

            String targetWord;
            String inputPath = null;

            if (args.length == 1) {
                // stdin
                targetWord = args[0];
                System.err.println("📥 Leyendo texto desde stdin...");
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                String ruta = buffer.toString();
                if (ruta.startsWith("\"") && ruta.endsWith("\"")) {
                    ruta = ruta.substring(1, ruta.length() - 1);
                }
                System.err.println(ruta);
                DirectoryWordCountResult result = useCase.countSpecificWordInDirectory(ruta, targetWord);
                reportWriter.writeDirectoryToStdout(result);

                Path outputFile = Paths.get("reporte_directorio.txt");
                reportWriter.writeDirectoryReport(result, outputFile.toString());
                System.out.println(outputFile.toAbsolutePath());
                System.out.flush();
                return;
            } else {
                inputPath = args[0];
                targetWord = args[1];
            }

            if (Files.isDirectory(Paths.get(inputPath))) {
                DirectoryWordCountResult result = useCase.countSpecificWordInDirectory(inputPath, targetWord);
                reportWriter.writeDirectoryToStdout(result);

                Path outputFile = Paths.get("reporte_directorio.txt");
                reportWriter.writeDirectoryReport(result, outputFile.toString());
                System.out.println(outputFile.toAbsolutePath());
                System.out.flush();
            } else if (Files.isRegularFile(Paths.get(inputPath))) {
                WordCountResult result = useCase.countSpecificWordInFile(inputPath, targetWord);
                reportWriter.writeToStdout(result);

                Path outputFile = Paths.get(inputPath + "_ocurrencias.txt");
                reportWriter.writeToFile(result, outputFile.toString());
            } else {
                System.err.println("❌ La ruta especificada no es válida: " + inputPath);
            }

        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            useCase.shutdown();
        }
    }
}