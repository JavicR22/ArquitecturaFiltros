package org.example.copier;

import org.example.io.OutputSink;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class PipelineFileCopier extends FileCopier {

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
