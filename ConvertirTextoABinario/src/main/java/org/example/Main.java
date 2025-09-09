package org.example;

import org.example.application.usecase.ConvertTextFileUseCase;
import org.example.infrastructure.TextToBinaryFileConverter;

import java.io.*;
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
                    useCase.execute(System.in, "stdin.bin");
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
                    useCase.execute(System.in, inputPath);
                } else {
                    // Archivo directo
                    useCase.execute(inputPath);
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
