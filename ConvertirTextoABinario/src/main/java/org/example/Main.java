package org.example;

import org.example.application.usecase.ConvertTextFileUseCase;
import org.example.infrastructure.TextToBinaryFileConverter;

import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        try {
            // ✅ Caso 1: entrada por stdin
            if (args.length == 0) {
                System.out.println("📥 Leyendo texto desde stdin...");

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.write(line.getBytes());
                        buffer.write(System.lineSeparator().getBytes());
                    }
                }

                byte[] binaryData = buffer.toByteArray();

                // Guardar en archivo local
                Path outputFile = Paths.get("salida.bin");
                Files.write(outputFile, binaryData);
                System.out.println("✅ Archivo binario guardado en: " + outputFile.toAbsolutePath());

                // Enviar también a stdout (para pipe)
                System.out.write(binaryData);
                System.out.flush();
                System.exit(0);
            }

            // ✅ Caso 2: con argumento (archivo de entrada)
            if (args.length == 1) {
                String inputPath = args[0];

                ConvertTextFileUseCase useCase = new ConvertTextFileUseCase(
                        new TextToBinaryFileConverter()
                );

                useCase.execute(inputPath);

                System.exit(0);
            }

            System.out.println("Uso:");
            System.out.println("  • Archivo: java -jar convertidorArchivoABinario.jar <ruta_del_archivo>");
            System.out.println("  • Pipe:    cat archivo.txt | java -jar convertidorArchivoABinario.jar");
            System.exit(1);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
