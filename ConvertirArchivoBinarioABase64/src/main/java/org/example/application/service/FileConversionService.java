package org.example.application.service;

import org.example.domain.entities.FileConversionResult;
import org.example.domain.usecases.ConvertBinaryFileToBase64UseCase;

public class FileConversionService {
    private final ConvertBinaryFileToBase64UseCase convertUseCase;

    public FileConversionService(ConvertBinaryFileToBase64UseCase convertUseCase) {
        this.convertUseCase = convertUseCase;
    }

    public FileConversionResult convertBinaryFileToBase64(String inputPath) {
        return convertUseCase.execute(inputPath);
    }

    public void printConversionResult(FileConversionResult result) {
        if (result.isSuccessful()) {
            System.out.println("✅ Conversión completada exitosamente!");
            System.out.println("📁 Archivo origen: " + result.getInputPath());
            System.out.println("📁 Archivo destino: " + result.getOutputPath());
            System.out.println("📏 Tamaño origen: " + result.getInputSize() + " bytes");
            System.out.println("📏 Tamaño destino: " + result.getOutputSize() + " bytes");
            System.out.println("🕐 Tiempo: " + result.getConversionTime());
        } else {
            System.err.println("❌ Error en la conversión: " + result.getErrorMessage());
        }
    }
}
