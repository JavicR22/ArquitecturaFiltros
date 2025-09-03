package org.example;

import org.example.application.service.FileConversionService;
import org.example.domain.entities.FileConversionResult;
import org.example.domain.entities.BatchConversionResult;
import org.example.infrastructure.config.DependencyInjection;

import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        DependencyInjection di = null;

        try {
            di = DependencyInjection.getInstance();
            FileConversionService service = di.getFileConversionService();

            // ✅ Caso 1: stdin → stdout + archivo local
            if (args.length == 0) {
                System.out.println("📥 Leyendo binario desde stdin...");

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (InputStream in = System.in) {
                    byte[] chunk = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = in.read(chunk)) != -1) {
                        buffer.write(chunk, 0, bytesRead);
                    }
                }

                byte[] binaryData = buffer.toByteArray();

                // Convertir a Base64
                String base64 = java.util.Base64.getEncoder().encodeToString(binaryData);

                // Guardar en archivo local
                Path outputFile = Paths.get("salida.base64");
                Files.write(outputFile, base64.getBytes());
                System.out.println("✅ Archivo Base64 guardado en: " + outputFile.toAbsolutePath());

                // Enviar también a stdout (para pipes)
                System.out.println(base64);
                System.exit(0);
            }

            // ✅ Caso 2: archivo o directorio
            if (args.length == 1) {
                String inputPath = args[0];

                if (Files.isDirectory(Paths.get(inputPath))) {
                    System.out.println("🚀 Iniciando conversión por lotes para el directorio: " + inputPath);
                    BatchConversionResult batchResult = service.convertDirectoryToBase64(inputPath);
                    service.printBatchConversionResult(batchResult);
                    System.exit(batchResult.hasFailures() ? 1 : 0);

                } else if (Files.isRegularFile(Paths.get(inputPath))) {
                    System.out.println("🚀 Iniciando conversión de archivo individual: " + inputPath);
                    FileConversionResult result = service.convertBinaryFileToBase64(inputPath);
                    service.printConversionResult(result);
                    System.exit(result.isSuccessful() ? 0 : 1);

                } else {
                    System.err.println("❌ La ruta especificada no es un archivo ni directorio válido: " + inputPath);
                    System.exit(1);
                }
            }

            System.out.println("Uso:");
            System.out.println("  • Archivo: java -jar ConvertirArchivoBinarioABase64.jar <ruta_del_archivo>");
            System.out.println("  • Directorio: java -jar ConvertirArchivoBinarioABase64.jar <ruta_del_directorio>");
            System.out.println("  • Pipe:    java -jar ConvertirArchivoBinarioABase64.jar < archivo.bin");
            System.exit(1);

        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        } finally {
            if (di != null) {
                di.shutdown();
            }
        }
    }
}
