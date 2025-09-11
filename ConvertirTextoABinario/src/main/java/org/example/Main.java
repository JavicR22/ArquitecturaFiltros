package org.example;

import org.example.application.usecase.ConvertTextFileUseCase;
import org.example.infrastructure.TextToBinaryFileConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        ConvertTextFileUseCase useCase = new ConvertTextFileUseCase(
                new TextToBinaryFileConverter()
        );

        try {
            if (args.length == 0) {
                // Si no hay args → stdin + nombre por defecto
                if (System.in.available() > 0) {
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


                    useCase.execute(ruta);
                } else {
                    System.out.println("Uso:");
                    System.out.println("  java -jar ConvertirTextoABinario.jar archivo.txt");
                    System.out.println("  cat archivo.txt | java -jar ConvertirTextoABinario.jar");
                    System.exit(1);
                }
            } else if (args.length == 1) {
                String inputPath = args[0];
                if (System.in.available() > 0) {
                    // Pipe con nombre de referencia
                    System.err.println("Holiii");

                } else {

                    // Archivo directo
                    useCase.execute(inputPath);
                    System.err.println(inputPath);
                    String envioRuta = buildBinPath(inputPath);
                    System.out.println(envioRuta);
                    System.out.flush();
                }
            } else {
                System.out.println("Uso inválido.");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    private static String buildBinPath(String originalPath) {
        Path inputFile = Paths.get(originalPath);
        String name = inputFile.getFileName().toString();

        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);

        Path parent = inputFile.getParent();
        if (parent == null) {
            parent = Paths.get("."); // directorio actual
        }

        // Siempre termina en .bin
        return parent.resolve(baseName + "Binario.bin").toString();
    }
}
