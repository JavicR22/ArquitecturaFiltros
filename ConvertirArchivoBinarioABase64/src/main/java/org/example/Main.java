package org.example;

import org.example.application.service.FileConversionService;
import org.example.domain.entities.FileConversionResult;
import org.example.domain.entities.BatchConversionResult;
import org.example.domain.usecases.BinarioABase64Process;
import org.example.infrastructure.config.DependencyInjection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {

        try {


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

                String ruta = new String(binaryData, StandardCharsets.UTF_8).trim();

                BinarioABase64Process binarioABase64Process = new BinarioABase64Process();
                binarioABase64Process.process(ruta);

            }

            if (args.length == 1) {
                String inputPath = args[0];
                BinarioABase64Process binarioABase64Process = new BinarioABase64Process();
                binarioABase64Process.process(inputPath);
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
        }
    }
}
