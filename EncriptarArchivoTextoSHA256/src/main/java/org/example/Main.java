package org.example;


import org.example.hashing.SHA256HashService;
import org.example.io.FileNameGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
                if (ruta.startsWith("\"") && ruta.endsWith("\"")) {
                    ruta = ruta.substring(1, ruta.length() - 1);
                }
                System.err.println("Son: "+ruta);
                Path inputFile = Paths.get(ruta);

                if (!Files.exists(inputFile)) {
                    System.err.println("❌ El archivo no existe: " + ruta);
                    System.exit(1);
                }

                // Leer bytes y calcular hash
                byte[] inputData;
                try (InputStream in = Files.newInputStream(inputFile)) {
                    inputData = in.readAllBytes();
                }
                String hash = hashService.hashBytes(inputData);

                // Imprimir en consola
                System.err.println(hash);

                hashService.process(ruta);

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
                System.err.println(hash);

                // Nombre nuevo: nombreOriginalEncriptadoSHA256.txt
                hashService.process(inputPath);


            } else {
                System.err.println("Uso: java -jar EncriptarArchivoTexto.jar [archivo.txt]");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("❌ Error al procesar: " + e.getMessage());
            System.exit(1);
        }
        }
}
