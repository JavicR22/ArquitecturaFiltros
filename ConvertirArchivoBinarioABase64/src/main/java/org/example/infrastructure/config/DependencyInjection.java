package org.example.infrastructure.config;

import org.example.application.service.FileConversionService;
import org.example.domain.ports.BinaryToBase64ConverterPort;
import org.example.domain.ports.DirectoryReaderPort;
import org.example.domain.ports.FileReaderPort;
import org.example.domain.ports.FileWriterPort;
import org.example.domain.usecases.ConvertBinaryFileToBase64UseCase;
import org.example.domain.usecases.ConvertDirectoryToBase64UseCase;
import org.example.infrastructure.adapters.BinaryToBase64FileConverter;
import org.example.infrastructure.adapters.LocalDirectoryReader;
import org.example.infrastructure.adapters.LocalFileReader;
import org.example.infrastructure.adapters.LocalFileWriter;

public class DependencyInjection {
    private static DependencyInjection instance;

    // Ports
    private final FileReaderPort fileReader;
    private final FileWriterPort fileWriter;
    private final BinaryToBase64ConverterPort converter;
    private final DirectoryReaderPort directoryReader;

    // Use Cases
    private final ConvertBinaryFileToBase64UseCase convertFileUseCase;
    private final ConvertDirectoryToBase64UseCase convertDirectoryUseCase;

    // Services
    private final FileConversionService fileConversionService;

    private DependencyInjection() {
        // Adapters
        this.fileReader = new LocalFileReader();
        this.fileWriter = new LocalFileWriter();
        this.converter = new BinaryToBase64FileConverter();
        this.directoryReader = new LocalDirectoryReader();

        // Use Cases
        this.convertFileUseCase = new ConvertBinaryFileToBase64UseCase(
                fileReader,
                fileWriter,
                converter
        );

        this.convertDirectoryUseCase = new ConvertDirectoryToBase64UseCase(
                directoryReader,
                convertFileUseCase
        );

        // Services
        this.fileConversionService = new FileConversionService(
                convertFileUseCase,
                convertDirectoryUseCase
        );
    }

    public static DependencyInjection getInstance() {
        if (instance == null) {
            synchronized (DependencyInjection.class) {
                if (instance == null) {
                    instance = new DependencyInjection();
                }
            }
        }
        return instance;
    }

    public FileConversionService getFileConversionService() {
        return fileConversionService;
    }

    public void shutdown() {
        fileConversionService.shutdown();
    }
}
