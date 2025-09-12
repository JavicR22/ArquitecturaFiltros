package org.example.util;

import org.example.IFiltro;
import org.example.processor.Processor;
import org.example.processor.ProcessorFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Base64ABinarioProcess implements IFiltro {

    @Override
    public void process(String filePath) throws IOException {
        Path inputPath = Paths.get(filePath);

        // 🔍 Validaciones
        if (!Files.exists(inputPath)) {
            throw new IOException("El archivo '" + filePath + "' no existe.");
        }
        if (!Files.isReadable(inputPath)) {
            throw new IOException("No se pued leer el archivo '" + filePath + "'. Verifique los permisos.");
        }
        if (Files.isDirectory(inputPath)) {
            throw new IOException("'" + filePath + "' es un directorio, no un archivo.");
        }
        if (Files.size(inputPath) == 0) {
            throw new IOException("El archivo '" + filePath + "' está vacío.");
        }

        // ⚙️ Crear processor y ejecutar
        Processor processor = ProcessorFactory.createFileProcessor(filePath);
        try {
            processor.process();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(filePath);
        System.out.flush();
    }
}
