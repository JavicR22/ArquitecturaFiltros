package org.example.util;

import org.example.copier.PipelineFileCopier;
import org.example.io.StdoutOutputSink;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CargarArchivoProcess {
    private String origen;
    public void process(String input) throws IOException{
        Path destino = Paths.get(input);
        PipelineFileCopier pipelineCopier = new PipelineFileCopier();
        StdoutOutputSink stdoutSink = new StdoutOutputSink();
        Path archivoCopiado = pipelineCopier.copyFromStdin(destino, stdoutSink);

        System.err.println("   Archivo copiado desde ruta recibida en: " + archivoCopiado.toAbsolutePath());
    }
}
