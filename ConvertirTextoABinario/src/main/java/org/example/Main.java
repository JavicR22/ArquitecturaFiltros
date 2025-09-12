package org.example;

import org.example.application.usecase.ConvertTextFileUseCase;
import org.example.infrastructure.TextToBinaryFileConverter;
import org.example.infrastructure.TextoABinarioProcess;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {


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
                    TextoABinarioProcess textoABinarioProcess= new TextoABinarioProcess();
                    textoABinarioProcess.process(ruta);


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

                    TextoABinarioProcess textoABinarioProcess =new TextoABinarioProcess();
                    textoABinarioProcess.process(inputPath);
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

}
