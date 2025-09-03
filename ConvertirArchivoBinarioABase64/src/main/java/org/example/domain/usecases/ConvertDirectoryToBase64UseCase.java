package org.example.domain.usecases;

import org.example.domain.entities.BatchConversionResult;
import org.example.domain.entities.BatchConversionResultBuilder;
import org.example.domain.entities.FileConversionResult;
import org.example.domain.ports.DirectoryReaderPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class ConvertDirectoryToBase64UseCase {
    private final DirectoryReaderPort directoryReader;
    private final ConvertBinaryFileToBase64UseCase convertFileUseCase;
    private final ExecutorService executorService;

    public ConvertDirectoryToBase64UseCase(DirectoryReaderPort directoryReader,
                                           ConvertBinaryFileToBase64UseCase convertFileUseCase) {
        this.directoryReader = directoryReader;
        this.convertFileUseCase = convertFileUseCase;
        // Número de hilos basado en procesadores disponibles
        int threadCount = Math.max(2, Runtime.getRuntime().availableProcessors());
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public BatchConversionResult execute(String directoryPath) {
        LocalDateTime startTime = LocalDateTime.now();

        BatchConversionResultBuilder resultBuilder = BatchConversionResultBuilder.create()
                .directoryPath(directoryPath)
                .startTime(startTime);

        try {
            validateInput(directoryPath);

            // Obtener lista de archivos del directorio
            List<String> allFiles = directoryReader.listFiles(directoryPath);

            // Filtrar solo archivos binarios
            List<String> binaryFiles = allFiles.stream()
                    .filter(this::isBinaryFileSafe)
                    .collect(Collectors.toList());

            if (binaryFiles.isEmpty()) {
                System.out.println("⚠️  No se encontraron archivos binarios en el directorio: " + directoryPath);
                return resultBuilder
                        .endTime(LocalDateTime.now())
                        .totalFiles(0)
                        .successfulConversions(0)
                        .failedConversions(0)
                        .results(List.of())
                        .build();
            }

            System.out.println("🔄 Procesando " + binaryFiles.size() + " archivos binarios con " +
                    ((ThreadPoolExecutor) executorService).getCorePoolSize() + " hilos...");

            // Procesar archivos en paralelo
            List<CompletableFuture<FileConversionResult>> futures = binaryFiles.stream()
                    .map(filePath -> CompletableFuture.supplyAsync(() -> {
                        System.out.println("📁 Procesando: " + filePath);
                        return convertFileUseCase.execute(filePath);
                    }, executorService))
                    .collect(Collectors.toList());

            // Esperar que todos los trabajos terminen
            List<FileConversionResult> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            // Calcular estadísticas
            long successful = results.stream()
                    .mapToLong(result -> result.isSuccessful() ? 1 : 0)
                    .sum();

            int failed = results.size() - (int) successful;

            return resultBuilder
                    .endTime(LocalDateTime.now())
                    .totalFiles(binaryFiles.size())
                    .successfulConversions((int) successful)
                    .failedConversions(failed)
                    .results(results)
                    .build();

        } catch (Exception e) {
            return resultBuilder
                    .endTime(LocalDateTime.now())
                    .totalFiles(0)
                    .successfulConversions(0)
                    .failedConversions(1)
                    .results(List.of())
                    .build();
        }
    }

    private void validateInput(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del directorio no puede estar vacía");
        }

        if (!directoryReader.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("La ruta no es un directorio válido: " + directoryPath);
        }
    }

    private boolean isBinaryFileSafe(String filePath) {
        try {
            return directoryReader.isBinaryFile(filePath);
        } catch (Exception e) {
            System.err.println("⚠️  Error verificando archivo: " + filePath + " - " + e.getMessage());
            return false;
        }
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
