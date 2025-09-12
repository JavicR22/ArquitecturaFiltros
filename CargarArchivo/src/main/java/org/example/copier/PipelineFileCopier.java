package org.example.copier;

import org.example.io.OutputSink;
import org.example.io.StdinInputSource;
import org.example.util.FileUtils;
import org.example.validator.TextFileValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class PipelineFileCopier extends FileCopier {

    public Path copyFromStdin(Path directorioDestino, OutputSink pipelineOutput) throws IOException {
        if (!Files.exists(directorioDestino) || !Files.isDirectory(directorioDestino)) {
            throw new IllegalArgumentException("El directorio de destino no existe: " + directorioDestino);
        }

        if (!Files.isWritable(directorioDestino)) {
            throw new IllegalArgumentException("No hay permisos de escritura en el directorio de destino: " + directorioDestino);
        }

        StdinInputSource stdinSource = new StdinInputSource();

        try {
            String pathString = stdinSource.readContent();
            Path archivoOrigen = stdinSource.getFilePath();

            TextFileValidator validator = new TextFileValidator();
            validator.validateFile(archivoOrigen);

            String originalFilename = archivoOrigen.getFileName().toString();
            Path destino = directorioDestino.resolve(originalFilename);
            destino = FileUtils.resolveConflict(destino);

            Files.copy(archivoOrigen, destino, StandardCopyOption.REPLACE_EXISTING);

            if (pipelineOutput != null) {
                try {
                    String destinationPath = destino.toAbsolutePath().toString();
                    pipelineOutput.writeContent(destinationPath.getBytes(StandardCharsets.UTF_8));
                } finally {
                    pipelineOutput.close();
                }
            }

            return destino;
        } finally {
            stdinSource.cleanup();
        }
    }

    public Path copyWithPipelineOutput(Path origen, Path directorioDestino, OutputSink pipelineOutput) throws IOException {
        Path archivoCopiado = super.copy(origen, directorioDestino);

        // Imprimir en consola la ruta del archivo copiado
        System.err.println("✅ Archivo copiado en: " + archivoCopiado.toAbsolutePath());

        // Leer el contenido del archivo original y escribirlo en el destino
        // sin necesidad de un buffer explícito (usar Files.copy hace esto internamente)
        // pero si quieres, lo hacemos manual con NIO:
        byte[] contenido = Files.readAllBytes(origen);
        Files.write(archivoCopiado, contenido, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return archivoCopiado;
    }
}
