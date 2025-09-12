package org.example;

import org.example.copier.PipelineFileCopier;
import org.example.io.StdoutOutputSink;
import org.example.validator.TextFileValidator;
import org.example.validator.ValidatorFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 1) {
            

            try {
                Path destino = Paths.get(args[0]);
                PipelineFileCopier pipelineCopier = new PipelineFileCopier();
                StdoutOutputSink stdoutSink = new StdoutOutputSink();
                Path archivoCopiado = pipelineCopier.copyFromStdin(destino, stdoutSink);

                System.err.println("   Archivo copiado desde ruta recibida en: " + archivoCopiado.toAbsolutePath());

            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                System.exit(1);
            }
        } else if (args.length == 2) {
            Path origen = Paths.get(args[0]);
            Path destino = Paths.get(args[1]);

            try {
                TextFileValidator validator = new TextFileValidator();
                validator.validateFile(origen);

                PipelineFileCopier pipelineCopier = new PipelineFileCopier();
                StdoutOutputSink stdoutSink = new StdoutOutputSink();
                Path archivoCopiado = pipelineCopier.copyWithPipelineOutput(origen, destino, stdoutSink);
                System.out.println(archivoCopiado);
                System.out.flush();
                System.err.println("   Archivo copiado con éxito en: " + archivoCopiado.toAbsolutePath());

            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                System.exit(1);
            }
        } else {
            System.err.println("Uso:");
            System.err.println("  Modo normal: java -jar programa.jar <ruta_origen> <directorio_destino>");
            System.err.println("  Modo pipeline: componente_anterior | java -jar programa.jar <directorio_destino>");
            System.err.println("    (El componente anterior debe enviar la ruta del archivo por stdout)");
            System.exit(1);
        }
    }
}
