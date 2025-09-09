package org.example;


import org.example.hashing.HashService;
import org.example.hashing.SHA256HashService;
import org.example.io.FileNameGenerator;
import org.example.io.InputReader;
import org.example.io.OutputWriter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        SHA256HashService hashService = new SHA256HashService();
        FileNameGenerator fileNameGenerator = new FileNameGenerator();

        try {
            if (args.length == 0) {
                // Caso STDIN
                System.out.println("📥 Leyendo desde stdin...");
                byte[] inputData = System.in.readAllBytes();
                String hash = hashService.hashBytes(inputData);

                // Imprimir siempre en consola
                System.out.println(hash);

                // Guardar en archivo
                Path outputFile = Paths.get("stdinEncriptadoSHA256.txt");
                Files.writeString(outputFile, hash);
                System.out.println("✅ Hash guardado en: " + outputFile.toAbsolutePath());

            } else if (args.length == 1) {
                // Caso archivo
                String inputPath = args[0];
                Path inputFile = Paths.get(inputPath);

                if (!Files.exists(inputFile)) {
                    System.err.println("❌ El archivo no existe: " + inputPath);
                    System.exit(1);
                }

                // Leer bytes y calcular hash
                byte[] inputData;
                try (InputStream in = Files.newInputStream(inputFile)) {
                    inputData = in.readAllBytes();
                }
                String hash = hashService.hashBytes(inputData);

                // Imprimir en consola
                System.out.println(hash);

                // Nombre nuevo: nombreOriginalEncriptadoSHA256.txt
                String outputFileName = fileNameGenerator.generate(inputFile, "EncriptadoSHA256", ".txt");
                Path outputFile = inputFile.getParent() == null
                        ? Paths.get(outputFileName)   // directorio actual
                        : inputFile.getParent().resolve(outputFileName);

                Files.writeString(outputFile, hash);
                System.out.println("✅ Hash guardado en: " + outputFile.toAbsolutePath());

            } else {
                System.out.println("Uso: java -jar EncriptarArchivoTexto.jar [archivo.txt]");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("❌ Error al procesar: " + e.getMessage());
            System.exit(1);
        }
        }
}
