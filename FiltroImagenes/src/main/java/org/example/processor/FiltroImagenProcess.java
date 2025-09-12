package org.example.processor;

import org.example.IFiltro;
import org.example.factory.ProcessorFactory;

import java.io.IOException;

public class FiltroImagenProcess implements IFiltro {

    @Override
    public void process(String directoryPath) throws IOException{
        ImageProcessor processor;
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: debe proporcionar una ruta de directorio válida por stdin");
        }

        directoryPath = directoryPath.trim();
        System.err.println("Procesando directorio: " + directoryPath);
        processor = ProcessorFactory.createEnhancedFileProcessor(directoryPath);

        processor.procesar();
        System.out.println(directoryPath);
        System.out.flush();
    }
}
