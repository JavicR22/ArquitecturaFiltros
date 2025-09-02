package org.example.infrastructure.config;

import org.example.application.service.FileConversionService;
import org.example.domain.ports.BinaryToBase64ConverterPort;
import org.example.domain.ports.FileReaderPort;
import org.example.domain.ports.FileWriterPort;
import org.example.domain.usecases.ConvertBinaryFileToBase64UseCase;
import org.example.infrastructure.adapters.BinaryToBase64FileConverter;
import org.example.infrastructure.adapters.LocalFileReader;
import org.example.infrastructure.adapters.LocalFileWriter;

public class DependencyInjection {
    private static DependencyInjection instance;

    private final FileReaderPort fileReader;
    private final FileWriterPort fileWriter;
    private final BinaryToBase64ConverterPort converter;
    private final ConvertBinaryFileToBase64UseCase useCase;
    private final FileConversionService service;

    private DependencyInjection() {
        // Initialize adapters
        this.fileReader = new LocalFileReader();
        this.fileWriter = new LocalFileWriter();
        this.converter = new BinaryToBase64FileConverter();

        // Initialize use case
        this.useCase = new ConvertBinaryFileToBase64UseCase(
                fileReader, fileWriter, converter
        );

        // Initialize service
        this.service = new FileConversionService(useCase);
    }

    public static synchronized DependencyInjection getInstance() {
        if (instance == null) {
            instance = new DependencyInjection();
        }
        return instance;
    }

    public FileConversionService getFileConversionService() {
        return service;
    }

    public ConvertBinaryFileToBase64UseCase getConvertBinaryFileToBase64UseCase() {
        return useCase;
    }
}
