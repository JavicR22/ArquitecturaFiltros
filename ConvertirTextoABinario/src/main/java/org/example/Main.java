package org.example;

import org.example.application.usecase.ConvertTextFileUseCase;
import org.example.infrastructure.TextToBinaryFileConverter;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java -jar convertidorArchivoABinario.jar <ruta_del_archivo>");
            System.exit(1);
        }

        String inputPath = args[0];

        ConvertTextFileUseCase useCase = new ConvertTextFileUseCase(
                new TextToBinaryFileConverter()
        );

        try {
            useCase.execute(inputPath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }
}
