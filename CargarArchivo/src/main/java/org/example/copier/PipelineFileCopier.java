package org.example.copier;

import org.example.io.OutputSink;
import org.example.io.StdinInputSource;
import org.example.util.FileUtils;

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
            String content = stdinSource.readContent();
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            Path destino = directorioDestino.resolve("stdin_output.txt");
            destino = FileUtils.resolveConflict(destino);

            Files.write(destino, contentBytes, StandardOpenOption.CREATE);

            if (pipelineOutput != null) {
                try {
                    pipelineOutput.writeContent(contentBytes);
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

        if (pipelineOutput != null) {
            try {
                String content = Files.readString(archivoCopiado, StandardCharsets.UTF_8);
                pipelineOutput.writeContent(content.getBytes(StandardCharsets.UTF_8));
            } finally {
                pipelineOutput.close();
            }
        }

        return archivoCopiado;
    }
}
