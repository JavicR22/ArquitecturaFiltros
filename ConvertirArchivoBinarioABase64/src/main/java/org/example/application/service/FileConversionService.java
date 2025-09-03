package org.example.application.service;

import org.example.domain.entities.BatchConversionResult;
import org.example.domain.entities.FileConversionResult;
import org.example.domain.usecases.ConvertBinaryFileToBase64UseCase;
import org.example.domain.usecases.ConvertDirectoryToBase64UseCase;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class FileConversionService {
    private final ConvertBinaryFileToBase64UseCase convertFileUseCase;
    private final ConvertDirectoryToBase64UseCase convertDirectoryUseCase;

    public FileConversionService(ConvertBinaryFileToBase64UseCase convertFileUseCase,
                                 ConvertDirectoryToBase64UseCase convertDirectoryUseCase) {
        this.convertFileUseCase = convertFileUseCase;
        this.convertDirectoryUseCase = convertDirectoryUseCase;
    }

    public FileConversionResult convertBinaryFileToBase64(String inputPath) {
        return convertFileUseCase.execute(inputPath);
    }

    public BatchConversionResult convertDirectoryToBase64(String directoryPath) {
        return convertDirectoryUseCase.execute(directoryPath);
    }

    public void printConversionResult(FileConversionResult result) {
        if (result.isSuccessful()) {
            System.out.println("✅ Conversión completada exitosamente!");
            System.out.println("📁 Archivo origen: " + result.getInputPath());
            System.out.println("📁 Archivo destino: " + result.getOutputPath());
            System.out.println("📏 Tamaño origen: " + formatFileSize(result.getInputSize()));
            System.out.println("📏 Tamaño destino: " + formatFileSize(result.getOutputSize()));
            System.out.println("🕐 Tiempo: " + result.getConversionTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } else {
            System.err.println("❌ Error en la conversión: " + result.getErrorMessage());
        }
    }

    public void printBatchConversionResult(BatchConversionResult result) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 RESUMEN DE CONVERSIÓN POR LOTES");
        System.out.println("=".repeat(60));
        System.out.println("📁 Directorio: " + result.getDirectoryPath());
        System.out.println("🕐 Hora inicio: " + result.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("🕐 Hora fin: " + result.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("⏱️  Tiempo total: " + formatDuration(result.getTotalProcessingTimeMillis()));
        System.out.println("📊 Total archivos: " + result.getTotalFiles());
        System.out.println("✅ Exitosos: " + result.getSuccessfulConversions());
        System.out.println("❌ Fallidos: " + result.getFailedConversions());

        if (result.getTotalFiles() > 0) {
            double successRate = (double) result.getSuccessfulConversions() / result.getTotalFiles() * 100;
            System.out.println("📈 Tasa de éxito: " + String.format("%.1f%%", successRate));
        }

        // Mostrar estadísticas de tamaño
        long totalInputSize = result.getResults().stream()
                .filter(FileConversionResult::isSuccessful)
                .mapToLong(FileConversionResult::getInputSize)
                .sum();

        long totalOutputSize = result.getResults().stream()
                .filter(FileConversionResult::isSuccessful)
                .mapToLong(FileConversionResult::getOutputSize)
                .sum();

        if (totalInputSize > 0) {
            System.out.println("📏 Total procesado: " + formatFileSize(totalInputSize));
            System.out.println("📏 Total generado: " + formatFileSize(totalOutputSize));
            double expansion = (double) totalOutputSize / totalInputSize;
            System.out.println("📈 Factor de expansión: " + String.format("%.2fx", expansion));
        }

        // Mostrar archivos fallidos
        if (result.getFailedConversions() > 0) {
            System.out.println("\n❌ ARCHIVOS FALLIDOS:");
            result.getResults().stream()
                    .filter(r -> !r.isSuccessful())
                    .forEach(r -> System.out.println("   • " + r.getInputPath() + " - " + r.getErrorMessage()));
        }

        System.out.println("=".repeat(60));
    }

    public void shutdown() {
        if (convertDirectoryUseCase != null) {
            convertDirectoryUseCase.shutdown();
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String formatDuration(long millis) {
        Duration duration = Duration.ofMillis(millis);
        if (duration.toMinutes() > 0) {
            return String.format("%d min %d seg", duration.toMinutes(), duration.getSeconds() % 60);
        } else {
            return duration.getSeconds() + " seg";
        }
    }
}
