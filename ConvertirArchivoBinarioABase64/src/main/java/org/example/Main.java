package org.example;

import org.example.application.service.FileConversionService;
import org.example.domain.entities.FileConversionResult;
import org.example.infrastructure.config.DependencyInjection;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar ConvertirArchivoBinarioABase64.jar <ruta_del_archivo>");
            System.exit(1);
        }

        String inputPath = args[0];

        try {
            DependencyInjection di = DependencyInjection.getInstance();
            FileConversionService service = di.getFileConversionService();

            FileConversionResult result = service.convertBinaryFileToBase64(inputPath);
            service.printConversionResult(result);

            System.exit(result.isSuccessful() ? 0 : 1);

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

}
